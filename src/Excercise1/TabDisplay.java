package Excercise1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TabDisplay extends Tab {
    private FlowPane flowPaneForRadioButtons;
    private RadioButton radioButtonPlayer, radioButtonGame;
    private VBox vBox;
    private GridPane gridPanePlayer, gridPaneGame;
    private TextField textFieldFirstName, textFieldLastName, textFieldAddress, textFieldPostalCode, textFieldProvince,
            textFieldPhoneNumber, textFieldGameTitle, textFieldLastPlayedOn, textFieldHighScore;
    private ComboBox<String> comboBoxPlayer, comboBoxGame, comboBoxGamesOfPlayer;

    private ObservableList<String> observableListPlayerNames, observableListGameNames;
    private ObservableList<Player> observableListPlayerObjects;
    private ObservableList<Game> observableListGameObjects;
    private ObservableList<PlayerAndGame> observableListGamesOfPlayerObjects;

    private Button buttonDisplayGame, buttonRefresh;
    private Player selectedPlayer;
    private Game selectedGame;
    private PlayerAndGame selectedGameOfPlayer;

    // Make the observableList local this class.
    public TabDisplay() {
        super();

        setObservableLists();

        setRadioButtons();
        setFlowPaneRadioButtons();

        setTextFields();
        setComboBoxes();

        setGridPanePlayer();
        setGridPaneGame();
        setVBox();

        setTab();

        setHandlers();

    }

    private void populateTextFieldsPlayer(int indexOfSelectedPlayer) {
        selectedPlayer = observableListPlayerObjects.get(indexOfSelectedPlayer);
        textFieldFirstName.setText(selectedPlayer.getFirstName());
        textFieldLastName.setText(selectedPlayer.getLastName());
        textFieldAddress.setText(selectedPlayer.getAddress());
        textFieldPostalCode.setText(selectedPlayer.getPostalCode());
        textFieldProvince.setText(selectedPlayer.getProvince());
        textFieldPhoneNumber.setText(selectedPlayer.getPhoneNumber());

    }

    private void setObservableLists() {
        observableListPlayerObjects = FXCollections.observableArrayList();
        observableListGameObjects = FXCollections.observableArrayList();
        observableListGamesOfPlayerObjects = FXCollections.observableArrayList();

    }

    private void refreshEditables() {

        if (observableListGamesOfPlayerObjects != null) {
            int tempSize = observableListGamesOfPlayerObjects.size();
            for (int i = 0; i < tempSize; i++) {
                observableListGamesOfPlayerObjects.remove(0);
                comboBoxGamesOfPlayer.getItems().remove(0);
            }
        }

    }

    private void updateComboBoxGamesOfPlayer() {
        textFieldLastPlayedOn.setText("");
        textFieldHighScore.setText("");

        Connection con = null;

        try {

            con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

            // Query for selecting joined table of Player and Game.
            PreparedStatement ps = con.prepareStatement(
                    "SELECT PlayerAndGame.player_game_id, Player.player_id, Player.first_name, Game.game_title, PlayerAndGame.playing_date, PlayerAndGame.score, Game.game_Id FROM Player INNER JOIN PlayerAndGame ON Player.player_id = PlayerAndGame.player_id INNER JOIN Game ON PlayerAndGame.game_id = Game.game_Id WHERE Player.player_id = ?");

            ps.setInt(1, selectedPlayer.getPlayerId());

            ResultSet resultSet = ps.executeQuery();

            refreshEditables();

            // Iterate through the data in the result set.
            // Populate observableLists.
            while (resultSet.next()) {
                // Create new PlayerAndGame object.
                PlayerAndGame playerAndGame = new PlayerAndGame(resultSet.getInt("player_game_id"),
                        resultSet.getInt("game_Id"), resultSet.getInt("player_id"), resultSet.getString("game_title"),
                        resultSet.getString("playing_date"), resultSet.getInt("score"));

                // Populate observableListGamesOfPlayerObjects with the newly created object playerAndGame.
                observableListGamesOfPlayerObjects.add(playerAndGame);

                // Populate comboBox.
                comboBoxGamesOfPlayer.getItems().add(playerAndGame.getGameTitle());
            }

            ps.close();
            System.out.println(" comboBoxGamesOfPlayer updated!");
        } catch (SQLException e) {
            System.err.println("Error in updateComboBox(): " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateTextFieldsGame(int indexOfSelectedGame) {
        selectedGame = observableListGameObjects.get(indexOfSelectedGame);
        textFieldGameTitle.setText(selectedGame.getGameTitle());
    }

    public void setComboBoxes() {

        observableListPlayerNames = FXCollections.observableArrayList();
        observableListGameNames = FXCollections.observableArrayList();
        FXCollections.observableArrayList();

        comboBoxPlayer = new ComboBox<>();
        comboBoxGamesOfPlayer = new ComboBox<>();
        comboBoxGame = new ComboBox<>();

        populateComboBoxes();
    }

    private void populateComboBoxes() {

        Connection con = null;

        try {
            // Establish the connection.
            Class.forName(ConnectURL.DB_DRIVER);
            con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

            // For Player.
            String querySelect = "SELECT * FROM Player";
            querySelectPlayer(con, querySelect);

            // For Game.
            querySelect = "SELECT * FROM Game";
            querySelectGame(con, querySelect);

        } catch (SQLException e) {
            System.err.println("Error in method populateComboBoxes(): " + e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void querySelectGame(Connection con, String querySelect) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()) {
            // Player.
            observableListGameNames.add(resultSet.getString("game_title"));

            // Create new Player object.
            Game game = new Game(resultSet.getInt("game_Id"), resultSet.getString("game_title"));

            // Populate observableListPlayerObjects with the newly created
            // object game.
            observableListGameObjects.add(game);

            comboBoxGame.getItems().add(game.getGameTitle());
        }
    }

    private void querySelectPlayer(Connection con, String querySelect) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()) {
            // Player.
            observableListPlayerNames.add(resultSet.getString("first_name"));

            // Create new Player object.
            Player player = new Player(resultSet.getInt("player_id"), resultSet.getString("first_name"),
                    resultSet.getString("last_name"), resultSet.getString("address"),
                    resultSet.getString("postal_code"), resultSet.getString("province"),
                    resultSet.getString("phone_number"));

            // Populate observableListPlayerObjects with the newly created object Player.
            observableListPlayerObjects.add(player);

            // Populate comboBox.
            comboBoxPlayer.getItems()
                    .add(player.getFirstName() + " " + player.getLastName() + ", " + player.getPlayerId());
        }
    }

    private void setTab() {
        this.setText("TabDisplay");
        this.setContent(vBox);
    }

    private void setGridPaneGame() {
        gridPaneGame = new GridPane();
        gridPaneGame.setAlignment(Pos.CENTER_LEFT);
        gridPaneGame.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPaneGame.setHgap(5.5);
        gridPaneGame.setVgap(5.5);

        // Place nodes in the pane
        gridPaneGame.add(new Label("Game to Display"), 0, 0);
        gridPaneGame.add(comboBoxGame, 1, 0);
        gridPaneGame.add(new Label("Game Title:"), 0, 1);
        gridPaneGame.add(textFieldGameTitle, 1, 1);

        buttonDisplayGame = new Button("Display Game");
        gridPaneGame.add(buttonDisplayGame, 1, 2);
    }

    private void setTextFields() {
        textFieldFirstName = new TextField();
        textFieldLastName = new TextField();
        textFieldAddress = new TextField();
        textFieldPostalCode = new TextField();
        textFieldProvince = new TextField();
        textFieldPhoneNumber = new TextField();
        textFieldGameTitle = new TextField();
        textFieldLastPlayedOn = new TextField();
        textFieldHighScore = new TextField();
    }

    private void setGridPanePlayer() {
        gridPanePlayer = new GridPane();
        gridPanePlayer.setAlignment(Pos.CENTER_LEFT);
        gridPanePlayer.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPanePlayer.setHgap(5.5);
        gridPanePlayer.setVgap(5.5);

        // Place nodes in the pane
        gridPanePlayer.add(new Label("Player to Display"), 0, 0);
        gridPanePlayer.add(comboBoxPlayer, 1, 0);
        gridPanePlayer.add(new Label("First Name:"), 0, 1);
        gridPanePlayer.add(textFieldFirstName, 1, 1);
        gridPanePlayer.add(new Label("Last Name:"), 0, 2);
        gridPanePlayer.add(textFieldLastName, 1, 2);
        gridPanePlayer.add(new Label("Address:"), 0, 3);
        gridPanePlayer.add(textFieldAddress, 1, 3);
        gridPanePlayer.add(new Label("Postal Code:"), 0, 4);
        gridPanePlayer.add(textFieldPostalCode, 1, 4);
        gridPanePlayer.add(new Label("Province:"), 0, 5);
        gridPanePlayer.add(textFieldProvince, 1, 5);
        gridPanePlayer.add(new Label("Phone number:"), 0, 6);
        gridPanePlayer.add(textFieldPhoneNumber, 1, 6);

        gridPanePlayer.add(new Label(""), 0, 7);
        gridPanePlayer.add(new Label(""), 1, 7);
        gridPanePlayer.add(new Label("Player's Game To Display"), 0, 8);
        gridPanePlayer.add(comboBoxGamesOfPlayer, 1, 8);
        gridPanePlayer.add(new Label("Last Played On"), 0, 9);
        gridPanePlayer.add(textFieldLastPlayedOn, 1, 9);
        gridPanePlayer.add(new Label("High Score"), 0, 10);
        gridPanePlayer.add(textFieldHighScore, 1, 10);

    }

    private void setVBox() {
        vBox = new VBox(20);
        vBox.setPadding(new Insets(15, 5, 5, 5));
        vBox.getChildren().add(flowPaneForRadioButtons);
        vBox.getChildren().add(gridPanePlayer);
    }

    private void setRadioButtons() {
        radioButtonPlayer = new RadioButton("Player");
        radioButtonPlayer.setSelected(true);
        radioButtonGame = new RadioButton("Game");

        ToggleGroup group = new ToggleGroup();
        radioButtonPlayer.setToggleGroup(group);
        radioButtonGame.setToggleGroup(group);

    }

    private void setFlowPaneRadioButtons() {
        buttonRefresh = new Button("Refresh");
        Label label = new Label("Select which info to display:");

        Button hiddenButton = new Button();
        hiddenButton.setPrefSize(ConnectURL.APP_WIDTH - 220, 25);
        hiddenButton.setVisible(false);

        flowPaneForRadioButtons = new FlowPane();
        flowPaneForRadioButtons.getChildren().addAll(label, hiddenButton, buttonRefresh, radioButtonPlayer,
                radioButtonGame);
    }

    private void setHandlers() {
        radioButtonPlayer.setOnAction(e ->
        {
            if (radioButtonPlayer.isSelected()) {
                if (gridPaneGame != null) {
                    vBox.getChildren().remove(gridPaneGame);
                    vBox.getChildren().add(gridPanePlayer);
                } else {
                    vBox.getChildren().add(gridPanePlayer);
                }
            }
        });

        radioButtonGame.setOnAction(e -> {
            if (radioButtonGame.isSelected()) {
                if (gridPanePlayer != null) {
                    vBox.getChildren().remove(gridPanePlayer);
                    vBox.getChildren().add(gridPaneGame);

                } else {
                    vBox.getChildren().add(gridPaneGame);
                }
            }
        });

        buttonRefresh.setOnAction(e -> {
            if (comboBoxPlayer != null) {
                refreshComboBoxes();
                populateComboBoxes();
                refreshEditables();
                refreshTextFields();
            }

        });

        comboBoxPlayer.setOnAction(e -> {
            populateTextFieldsPlayer(comboBoxPlayer.getSelectionModel().getSelectedIndex());
            updateComboBoxGamesOfPlayer();
        });

        comboBoxGamesOfPlayer.setOnAction(
                e -> populateTextFieldsGamesOfPlayer(comboBoxGamesOfPlayer.getSelectionModel().getSelectedIndex()));

        comboBoxGame.setOnAction(e -> populateTextFieldsGame(comboBoxGame.getSelectionModel().getSelectedIndex()));
    }

    private void refreshTextFields() {
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldAddress.setText("");
        textFieldPostalCode.setText("");
        textFieldProvince.setText("");
        textFieldPhoneNumber.setText("");
        textFieldGameTitle.setText("");
        textFieldLastPlayedOn.setText("");
        textFieldHighScore.setText("");

    }

    private void refreshComboBoxes() {
        int tempSize = comboBoxPlayer.getItems().size();
        for (int i = 0; i < tempSize; i++) {
            comboBoxPlayer.getItems().remove(0);
            observableListPlayerNames.remove(0);
            observableListPlayerObjects.remove(0);
        }

        tempSize = comboBoxGame.getItems().size();
        for (int i = 0; i < tempSize; i++) {
            comboBoxGame.getItems().remove(0);
            observableListGameNames.remove(0);
            observableListGameObjects.remove(0);
        }

    }

    private void populateTextFieldsGamesOfPlayer(int selectedIndex) {
        selectedGameOfPlayer = observableListGamesOfPlayerObjects.get(selectedIndex);
        textFieldLastPlayedOn.setText(selectedGameOfPlayer.getPlayingDate());
        textFieldHighScore.setText(Integer.toString(selectedGameOfPlayer.getScore()));
    }

}