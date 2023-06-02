package main;

import java.sql.Date;

public class Match {
    private String p1_name;
    private String p1_email;
    private String p2_name;
    private String p2_email;
    private int p1_games_won;
    private int p2_games_won;
    private Date date;
    private int court_number;
    private String venue_name;

    public Match(String p1_name, String p2_name, int p1_games_won, int p2_games_won,
                 Date date, int court_number, String venue_name) {
        this.p1_name = p1_name;
        this.p2_name = p2_name;
        this.p1_games_won = p1_games_won;
        this.p2_games_won = p2_games_won;
        this.date = date;
        this.court_number = court_number;
        this.venue_name = venue_name;
    }

    public String getP1_name() {
        return p1_name;
    }

    public void setP1Name(String p1_name) {
        this.p1_name = p1_name;
    }

    public String getP1_email() {
        return p1_email;
    }

    public void setP1Email(String p1_email) {
        this.p1_email = p1_email;
    }

    public String getP2_name() {
        return p2_name;
    }

    public void setP2Name(String p2_name) {
        this.p2_name = p2_name;
    }

    public String getP2_email() {
        return p2_email;
    }

    public void setP2Email(String p2_email) {
        this.p2_email = p2_email;
    }

    public String getP1_games_won() {
        return String.valueOf(p1_games_won);
    }

    public void setP1GamesWon(int p1_games_won) {
        this.p1_games_won = p1_games_won;
    }

    public int getP2_games_won() {
        return p2_games_won;
    }

    public void setP2GamesWon(int p2_games_won) {
        this.p2_games_won = p2_games_won;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCourt_number() {
        return court_number;
    }

    public void setCourtNumber(int court_number) {
        this.court_number = court_number;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenueName(String venue_name) {
        this.venue_name = venue_name;
    }
}
