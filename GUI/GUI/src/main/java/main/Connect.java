package main;

import java.sql.*;
import java.util.ArrayList;

public class Connect {

    private String db_url = "jdbc:mariadb://bp50.host.cs.st-andrews.ac.uk/bp50_cs3101_p2_db";
    //private String db_url = "jdbc:mariadb://localhost:3306/bp50_cs3101_p2_db";
    private String user = "bp50";
    private String password = "ef332efV5k9!Y9";
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rs;

    public ArrayList<League> getLeagues() {
        ArrayList<League> leagues = new ArrayList<League>();
        try {
            conn = DriverManager.getConnection(db_url, user, password);
            String queryStr = "SELECT name, year from league";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryStr);
            while(rs.next()) {
                leagues.add(new League(rs.getString("name"), rs.getInt("year")));
            }
            stmt.close();
            conn.close();
        } catch(Exception e) {
            System.out.printf("Exception: %s", e.toString());
        }
        return leagues;
    }

    public ArrayList<Match> getMatches(String league_name, int league_year) {
        ArrayList<Match> matches = new ArrayList<Match>();
        try {
            conn = DriverManager.getConnection(db_url, user, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * from view_matches_with_fullname where league_name = ? AND league_year = ?");
            stmt.setString(1, league_name);
            stmt.setInt(2, league_year);
            rs = stmt.executeQuery();
            while(rs.next()) {
                matches.add(new Match(rs.getString("p1_name"), rs.getString("p2_name"),
                        rs.getInt("p1_games_won"), rs.getInt("p2_games_won"),
                        rs.getDate("date_played"), rs.getInt("court_number"),
                        rs.getString("venue_name")));
            }
            stmt.close();
            conn.close();
        } catch(Exception e) {
            System.out.printf("Exception: %s", e.toString());
        }
        return matches;
    }

    public void addMatch(Match match, String league_name, int league_year) throws SQLException {
        conn = DriverManager.getConnection(db_url, user, password);
        PreparedStatement stmt = conn.prepareStatement("insert into played_match (p1_email, p2_email, "
                + "p1_games_won, "
                + "p2_games_won, date_played, "
                + "court_number, venue_name, league_name, league_year) values(?,?,?,?,?,?,?,?,?)");
        stmt.setString(1, match.getP1_email());
        stmt.setString(2, match.getP2_email());
        stmt.setInt(3, Integer.parseInt(match.getP1_games_won()));
        stmt.setInt(4, match.getP2_games_won());
        stmt.setDate(5, Date.valueOf(match.getDate()));
        stmt.setInt(6, match.getCourt_number());
        stmt.setString(7, match.getVenue_name());
        stmt.setString(8, league_name);
        stmt.setInt(9, league_year);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}