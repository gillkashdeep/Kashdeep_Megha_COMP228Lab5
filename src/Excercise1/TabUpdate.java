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
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TabUpdate extends Tab {
    private FlowPane flowPaneForRadioButtons;
    private RadioButton radioButtonPlayer, radioButtonGame;
    private VBox vBox;
    private GridPane gridPanePlayer, gridPaneGame;
    private TextField textFieldFirstName, textFieldLastName, textFieldAddress, textFieldPostalCode, textFieldProvince,
            textFieldPhoneNumber, textFieldGameTitle;
    private ComboBox<String> comboBoxPlayer, comboBoxGame;
    private ObservableList<String> observableListPlayerNames, observableListGameNames;
    private ObservableList<Player> observableListPlayerObjects;
    private ObservableList<Game> observableListGameObjects;

    private Button buttonUpdatePlayer, buttonUpdateGame;
    private Game selectedGame;
    private Player selectedPlayer;

    private GridPane gridPaneGameLibrary;
    private ListView<String> listViewGameChoices;
    private ListView<String> listViewGamesPlayed;
    private Button buttonAddGameToLib;
    private Button buttonRemoveGameFromLib;
    private ObservableList<PlayerAndGame> observableArrayListGamesOfPlayerAddOn;
    private ObservableList<Game> observableArrayListGameChoicesAddOn;

    public TabUpdate() {
        super();

        setObservableLists();

        setFlowPaneRadioButtons();
        setRadioButtons();

        setTextFields();
        setComboBoxes();
        setListViews();

        setGridAddOn();
        setGridPanePlayer();

        setGridPaneGame();

        setHandlers();
        setVBox();
        setTab();
    }

    private void setListViews() {
        //player games
        listViewGamesPlayed = new ListView<>();
        listViewGamesPlayed.setPrefSize(200, 200);

        // creating a list of games
        listViewGameChoices = new ListView<>();
        listViewGameChoices.setPrefSize(200, 200);

    }

    private void setObservableLists() {
        observableListPlayerObjects = FXCollections.observableArrayList();
        observableListGameObjects = FXCollections.observableArrayList();

        observableArrayListGamesOfPlayerAddOn = FXCollections.observableArrayList();
        observableArrayListGameChoicesAddOn = FXCollections.observableArrayList();

    }

    private void setGridAddOn() {
        setGridPaneGameLibrary();
        setGameLibraryObservableArrayLists();
        setGameLibraryHandlers();

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

    private void populateTextFieldsGame(int indexOfSelectedGame) {
        selectedGame = observableListGameObjects.get(indexOfSelectedGame);
        textFieldGameTitle.setText(selectedGame.getGameTitle());
    }

    public void setComboBoxes() {
        observableListPlayerNames = FXCollections.observableArrayList();
        observableListGameNames = FXCollections.observableArrayList();
        FXCollections.observableArrayList();
        comboBoxPlayer = new ComboBox<>();
        comboBoxGame = new ComboBox<>();

        populateComboBoxes();
    }

    private void populateComboBoxes() {
        Connection con = null;

        try {
            // Establish the connection.
            con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

            // For Player comboBox.
            String querySelect = "SELECT * FROM Player";
            querySelectPlayer(con, querySelect);

            // For Game comboBox.
            querySelect = "SELECT * FROM Game";
            querySelectGame(con, querySelect);

        } catch (SQLException e) {
            System.err.println("Error: " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
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
            // object Player.
            observableListGameObjects.add(game);

            // Populate comboBox.
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

            // Populate observableListPlayerObjects with the newly created
            // object Player.
            observableListPlayerObjects.add(player);

            // Populate comboBox.
            comboBoxPlayer.getItems()
                    .add(player.getFirstName() + " " + player.getLastName() + ", " + player.getPlayerId());
        }
    }

    private void setTab() {
        this.setText("TabUpdate");
        this.setContent(vBox);
    }

    private void setGridPaneGame() {
        gridPaneGame = new GridPane();
        gridPaneGame.setAlignment(Pos.CENTER);
        gridPaneGame.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPaneGame.setHgap(5.5);
        gridPaneGame.setVgap(5.5);

        // Place nodes in the pane
        gridPaneGame.add(new Label("Game to Display"), 0, 0);
        gridPaneGame.add(comboBoxGame, 1, 0);
        gridPaneGame.add(new Label("Game Title:"), 0, 1);
        gridPaneGame.add(textFieldGameTitle, 1, 1);

        buttonUpdateGame = new Button("Update Game");
        gridPaneGame.add(buttonUpdateGame, 1, 2);

    }

    private void setGridPaneGameLibrary() {
        gridPaneGameLibrary = new GridPane();
        gridPaneGameLibrary.setPrefSize(450, 200);

        gridPaneGameLibrary.setAlignment(Pos.CENTER);
        gridPaneGameLibrary.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPaneGameLibrary.setHgap(5.5);
        gridPaneGameLibrary.setVgap(5.5);

        // Place nodes in the pane
        gridPaneGameLibrary.add(new Label("Games you can play."), 0, 0);
        gridPaneGameLibrary.add(new Label(""), 1, 0);
        gridPaneGameLibrary.add(new Label("Games you currently play."), 2, 0);

        gridPaneGameLibrary.add(new ScrollPane(listViewGameChoices), 0, 1);

        buttonAddGameToLib = new Button("->");
        buttonRemoveGameFromLib = new Button("<-");
        buttonAddGameToLib.disableProperty();
        buttonRemoveGameFromLib.disableProperty();

        FlowPane flowPaneForButtons = new FlowPane();
        flowPaneForButtons.getChildren().add(buttonAddGameToLib);
        flowPaneForButtons.getChildren().add(buttonRemoveGameFromLib);
        flowPaneForButtons.setPrefSize(50, 60);
        gridPaneGameLibrary.add(flowPaneForButtons, 1, 1);
        GridPane.setValignment(flowPaneForButtons, VPos.CENTER);

        gridPaneGameLibrary.add(new ScrollPane(listViewGamesPlayed), 2, 1);
    }

    private void setGameLibraryObservableArrayLists() {
        if (selectedPlayer == null) {
            return;
        }

        if (observableArrayListGamesOfPlayerAddOn != null) {
            refreshEditables();
        }

        Connection con = null;

        try {
            // Establish the connection.
            Class.forName(ConnectURL.DB_DRIVER);
            con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

            // For game choices.
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM Game WHERE Game.game_Id NOT IN (SELECT PlayerAndGame.game_id FROM PlayerAndGame WHERE PlayerAndGame.player_id = ?)");

            // set the prepared statement parameters
            ps.setInt(1, selectedPlayer.getPlayerId());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                // Create new Game object.
                Game game = new Game(resultSet.getInt("game_Id"), resultSet.getString("game_title"));
                observableArrayListGameChoicesAddOn.add(game);
                listViewGameChoices.getItems().add(game.getGameTitle());
            }

            // For games that player can add.
            // Query for selecting joined table of Player and Game.
            ps = con.prepareStatement(
                    "SELECT PlayerAndGame.player_game_id, Player.player_id, Player.first_name, Game.game_title, PlayerAndGame.playing_date, PlayerAndGame.score, Game.game_Id FROM Player INNER JOIN PlayerAndGame ON Player.player_id = PlayerAndGame.player_id INNER JOIN Game ON PlayerAndGame.game_id = Game.game_Id WHERE Player.player_id = ?");

            // set the prepared statement parameters
            ps.setInt(1, selectedPlayer.getPlayerId());

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                // Create new PlayerAndGame object.
                PlayerAndGame playerAndGame = new PlayerAndGame(resultSet.getInt("player_game_id"),
                        resultSet.getInt("game_Id"), resultSet.getInt("player_id"), resultSet.getString("game_title"),
                        resultSet.getString("playing_date"), resultSet.getInt("score"));

                // Populate observableListGamesOfPlayerObjects with the newly
                // created object playerAndGame.
                observableArrayListGamesOfPlayerAddOn.add(playerAndGame);

                // Populate comboBox.
                listViewGamesPlayed.getItems().add(playerAndGame.getGameTitle());
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println("Error in populateComboBoxes(): " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshEditables() {
        if (observableArrayListGameChoicesAddOn != null) {
            int tempSize = observableArrayListGameChoicesAddOn.size();
            for (int i = 0; i < tempSize; i++) {
                observableArrayListGameChoicesAddOn.remove(0);
                listViewGameChoices.getItems().remove(0);
            }
        }

        if (observableArrayListGamesOfPlayerAddOn != null) {
            int tempSize = observableArrayListGamesOfPlayerAddOn.size();
            for (int i = 0; i < tempSize; i++) {
                observableArrayListGamesOfPlayerAddOn.remove(0);
                listViewGamesPlayed.getItems().remove(0);
            }
        }
    }

    private void setGameLibraryHandlers() {
        /*buttonAddGameToLib.setOnAction(e -> {
            if (selectedPlayer == null) {
                return;
            }
            insertPlayerAndGameToDb(listViewGameChoices.getSelectionModel().getSelectedIndex());
            refreshEditables();
            setGameLibraryObservableArrayLists();
        });

        buttonRemoveGameFromLib.setOnAction(e -> {
            if (selectedPlayer == null) {
                return;
            }
            removePlayerAndGameFromDb(listViewGamesPlayed.getSelectionModel().getSelectedIndex());
            refreshEditables();
            setGameLibraryObservableArrayLists();
        });
*/
    }

    private void insertPlayerAndGameToDb(int indexOfSelectedGame) {
        // Insert to db.
        try {
            PreparedStatement pst;
            Connection con;
            Class.forName(ConnectURL.DB_DRIVER);

            // establish connection to database
            con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

            pst = con.prepareStatement("Insert into [dbo].[PlayerAndGame] (game_id, player_id) VALUES(?,?)");
            // populate the fields
            pst.setInt(1, observableArrayListGameChoicesAddOn.get(indexOfSelectedGame).getGameId());
            pst.setInt(2, selectedPlayer.getPlayerId());

            // Execute the prepared statement using executeUpdate method:
            pst.executeUpdate(); // returns the row count

            System.out.println("method insertPlayerAndGameToDb() successful!");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            System.out.println("Done!");
        }

    }

    private void removePlayerAndGameFromDb(int indexOfSelectedGame) {
        try {
            PreparedStatement pst;
            Connection con;

            Class.forName(ConnectURL.DB_DRIVER);

            // establish connection to database
            con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

            pst = con.prepareStatement("DELETE FROM PlayerAndGame WHERE PlayerAndGame.player_game_id = ?");
            // populate the fields
            pst.setInt(1, observableArrayListGamesOfPlayerAddOn.get(indexOfSelectedGame).getPlayerGameId());

            // Execute the prepared statement using executeUpdate method:
            pst.executeUpdate(); // returns the row count

            System.out.println("method removePlayerAndGameFromDb() successful!");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            System.out.println("Done!");
        }

    }

    private void setTextFields() {
        textFieldFirstName = new TextField();
        textFieldLastName = new TextField();
        textFieldAddress = new TextField();
        textFieldPostalCode = new TextField();
        textFieldProvince = new TextField();
        textFieldPhoneNumber = new TextField();
        textFieldGameTitle = new TextField();
    }

    private void setGridPanePlayer() {
        gridPanePlayer = new GridPane();
        gridPanePlayer.setAlignment(Pos.CENTER);
        gridPanePlayer.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPanePlayer.setHgap(5.5);
        gridPanePlayer.setVgap(5.5);

        // Place nodes in the pane
        gridPanePlayer.add(new Label("Player to Update"), 0, 0);
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

        buttonUpdatePlayer = new Button("Update Player");
        gridPanePlayer.add(buttonUpdatePlayer, 1, 7);

    }

    private void setVBox() {
        vBox = new VBox(20);
        vBox.setPadding(new Insets(15, 5, 5, 5));
        vBox.getChildren().add(flowPaneForRadioButtons);
        vBox.getChildren().add(gridPanePlayer);
        vBox.getChildren().add(gridPaneGameLibrary);
    }

    private void setRadioButtons() {
        radioButtonPlayer = new RadioButton("Player");
        radioButtonPlayer.setSelected(true);
        radioButtonGame = new RadioButton("Game");

        ToggleGroup group = new ToggleGroup();
        radioButtonPlayer.setToggleGroup(group);
        radioButtonGame.setToggleGroup(group);

        flowPaneForRadioButtons.getChildren().addAll(radioButtonPlayer, radioButtonGame);

    }

    private void setFlowPaneRadioButtons() {
        flowPaneForRadioButtons = new FlowPane();
    }

    private void setHandlers() {
        radioButtonPlayer.setOnAction(e -> {
            if (radioButtonPlayer.isSelected()) {
                vBox.getChildren().remove(gridPaneGame);
                vBox.getChildren().add(gridPanePlayer);
                vBox.getChildren().add(gridPaneGameLibrary);
            }
        });

        radioButtonGame.setOnAction(e -> {
            if (radioButtonGame.isSelected()) {
                vBox.getChildren().remove(gridPanePlayer);
                vBox.getChildren().remove(gridPaneGameLibrary);
                vBox.getChildren().add(gridPaneGame);
            }
        });

        comboBoxPlayer.setOnAction(e -> {
            populateTextFieldsPlayer(comboBoxPlayer.getSelectionModel().getSelectedIndex());
            setGameLibraryObservableArrayLists();
        });
        comboBoxGame.setOnAction(e -> populateTextFieldsGame(comboBoxGame.getSelectionModel().getSelectedIndex()));
        buttonUpdateGame.setOnAction(e -> updateGame());
        buttonUpdatePlayer.setOnAction(e -> updatePlayer());
    }

    private void updatePlayer() {
        // Check if firstName and phoneNumber is null.
        if (textFieldFirstName.getText().trim().isEmpty() || textFieldPhoneNumber.getText().trim().isEmpty()) {
            System.out.println("first name or phone number cant be null");
        } else {
            Connection con = null;
            PreparedStatement ps = null;

            // Insert to db.
            try {
                Class.forName(ConnectURL.DB_DRIVER);

                // establish connection to database
                con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

                ps = con.prepareStatement(
                        "UPDATE Player " + "SET first_name = ?, " + "last_name = ?, " + "address = ?, "
                                + "postal_code = ?, " + "province = ?, " + "phone_number = ? " + "WHERE player_id = ?");

                ps.setString(1, textFieldFirstName.getText());
                ps.setString(2, textFieldLastName.getText());
                ps.setString(3, textFieldAddress.getText());
                ps.setString(4, textFieldPostalCode.getText());
                ps.setString(5, textFieldProvince.getText());
                ps.setString(6, textFieldPhoneNumber.getText());
                ps.setInt(7, selectedPlayer.getPlayerId());

                ps.executeUpdate();
                ps.close();

                System.out.println("updated!");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            }
        }
    }

    private void updateGame() {
        if (textFieldGameTitle.getText().trim().isEmpty()) {
            System.out.println("game title cant be null");
        } else {

            // Insert to db.
            try {
                Connection con;
                Class.forName(ConnectURL.DB_DRIVER);

                // establish connection to database
                con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

                PreparedStatement ps = con.prepareStatement("UPDATE Game SET game_title = ? WHERE game_Id = ?");

                ps.setString(1, textFieldGameTitle.getText());
                ps.setInt(2, selectedGame.getGameId());

                ps.executeUpdate();
                ps.close();

                System.out.println("updated!");

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            } finally {
                System.out.println("Done!");
            }
        }
    }

}
