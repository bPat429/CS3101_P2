package main;

import main.Connect;
import main.League;
import main.Match;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;


public class Controller implements Initializable {
    private Connect connect;
    private League currentLeague;
    private ArrayList<Match> matches = new ArrayList<Match>();

    @FXML // fx:id="leagueCombo"
    private ComboBox<String> leagueCombo; // Value injected by FXMLLoader

    @FXML // fx:id="matchTable"
    private TableView matchTable;

    @FXML // fx:id="changeView"
    private Button changeView;

    @FXML // fx:id="addMatchForm"
    private VBox addMatchForm;

    @FXML // fx:id="addMatch"
    private Button addMatch;

    @FXML // fx:id="p1_email_form"
    private TextField p1_email_form;

    @FXML // fx:id="p2_email_form"
    private TextField p2_email_form;

    @FXML // fx:id="p1_score_form"
    private TextField p1_score_form;

    @FXML // fx:id="p2_score_form"
    private TextField p2_score_form;

    @FXML // fx:id="date_form"
    private DatePicker date_form;

    @FXML // fx:id="court_number_form"
    private TextField court_number_form;

    @FXML // fx:id="venue_name_form"
    private TextField venue_name_form;

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        TextArea messageArea = new TextArea(message);
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(messageArea);
        alert.initOwner(owner);
        alert.show();
    }



    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        connect = new Connect();
        ArrayList<League> leagues = connect.getLeagues();
        String[] league_names = new String[leagues.size()];
        for (int i = 0; i < leagues.size(); i++) {
            league_names[i] = leagues.get(i).toString();
        }

        leagueCombo.getItems().setAll(league_names);

        TableColumn<Match, String> column1 = new TableColumn<>("P1 Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("p1_name"));
        TableColumn<Match, String> column2 = new TableColumn<>("P2 Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("p2_name"));
        TableColumn<Match, String> column3 = new TableColumn<>("P1 score");
        column3.setCellValueFactory(new PropertyValueFactory<>("p1_games_won"));
        TableColumn<Match, Integer> column4 = new TableColumn<>("P2 score");
        column4.setCellValueFactory(new PropertyValueFactory<>("p2_games_won"));
        TableColumn<Match, String> column5 = new TableColumn<>("Date");
        column5.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Match, Integer> column6 = new TableColumn<>("Court number");
        column6.setCellValueFactory(new PropertyValueFactory<>("court_number"));
        TableColumn<Match, String> column7 = new TableColumn<>("Venue Name");
        column7.setCellValueFactory(new PropertyValueFactory<>("venue_name"));

        matchTable.getColumns().add(column1);
        matchTable.getColumns().add(column2);
        matchTable.getColumns().add(column3);
        matchTable.getColumns().add(column4);
        matchTable.getColumns().add(column5);
        matchTable.getColumns().add(column6);
        matchTable.getColumns().add(column7);

        changeView.setVisible(false);
        addMatchForm.setVisible(false);
        addMatchForm.setManaged(false);

        // listen for changes to the fruit combo box selection and update the displayed fruit image accordingly.
        leagueCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldLeague, String newLeague) {
                if (newLeague != null) {
                    if (!changeView.isVisible()) {
                        changeView.setVisible(true);
                    }
                    for (int i = 0; i < leagues.size(); i++) {
                        if (newLeague.equals(leagues.get(i).toString())) {
                            currentLeague = leagues.get(i);
                            matches = connect.getMatches(leagues.get(i).getName(), leagues.get(i).getYear());
                            matchTable.getItems().clear();
                            if (matches != null && matches.size() > 0) {
                                for (Match match : matches) {
                                    matchTable.getItems().add(match);
                                }
                            }
                            i = leagues.size();
                        }
                    }
                }
            }
        });

        changeView.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (changeView.getText().equals("Add Match")) {
                    changeView.setText("See Matches");
                    matchTable.setVisible(false);
                    matchTable.setManaged(false);
                    addMatchForm.setVisible(true);
                    addMatchForm.setManaged(true);
                } else {
                    // Refresh played_matches table
                    matches = connect.getMatches(currentLeague.getName(), currentLeague.getYear());
                    matchTable.getItems().clear();
                    if (matches != null && matches.size() > 0) {
                        for (Match match : matches) {
                            matchTable.getItems().add(match);
                        }
                    }
                    changeView.setText("Add Match");
                    matchTable.setVisible(true);
                    matchTable.setManaged(true);
                    addMatchForm.setVisible(false);
                    addMatchForm.setManaged(false);
                }

            }
        });

        addMatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (p1_email_form.getText().isEmpty() || p2_email_form.getText().isEmpty() ||
                        p1_score_form.getText().isEmpty() || p2_score_form.getText().isEmpty() ||
                        date_form.getValue() == null || court_number_form.getText().isEmpty() ||
                        venue_name_form.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, addMatchForm.getScene().getWindow(),
                            "Error inserting Match", "Please fill in all fields");
                    return;
                }
                Match new_match = null;
                try {
                    Date date = Date.valueOf(date_form.getValue().format(ISO_LOCAL_DATE));
                    new_match = new Match(p1_email_form.getText(), p2_email_form.getText(),
                            Integer.parseInt(p1_score_form.getText()), Integer.parseInt(p2_score_form.getText()),
                            date, Integer.parseInt(court_number_form.getText()),
                            venue_name_form.getText());
                    new_match.setP1Email(p1_email_form.getText());
                    new_match.setP2Email(p2_email_form.getText());
                } catch (Exception exception) {
                    showAlert(Alert.AlertType.ERROR, addMatchForm.getScene().getWindow(),
                            "Error parsing input", "Error: " + exception.toString());
                    return;
                }

                try {
                    connect.addMatch(new_match, currentLeague.getName(), currentLeague.getYear());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    System.out.println(exception.toString());
                    showAlert(Alert.AlertType.ERROR, addMatchForm.getScene().getWindow(),
                            "Error inserting Match", "Error: " + exception.toString());
                    return;
                }
                showAlert(Alert.AlertType.CONFIRMATION, addMatchForm.getScene().getWindow(), "Insert Successful", "Match Added");
            }
        });
    }
}