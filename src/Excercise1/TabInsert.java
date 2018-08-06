package Excercise1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TabInsert extends Tab {
    private FlowPane flowPaneForRadioButtons;
    private RadioButton radioButtonPlayer, radioButtonGame;
    private VBox vBox;
    private GridPane gridPanePlayer, gridPaneGame;
    private TextField textFieldFirstName, textFieldLastName, textFieldAddress, textFieldPostalCode, textFieldProvince,
            textFieldPhoneNumber, textFieldGameTitle;
    private Button buttonInsertPlayer, buttonInsertGame, buttonRefresh;

    public TabInsert() {
        super();

        setRadioButtons();
        setFlowPaneRadioButtons();

        setTextFields();
        setGridPanePlayer();
        setGridPaneGame();
        setVBox();

        setTab();

        setHandlers();
    }

    private void setTab() {
        this.setText("TabInsert");
        this.setContent(vBox);
    }

    private void setGridPaneGame() {
        gridPaneGame = new GridPane();
        gridPaneGame.setAlignment(Pos.CENTER_LEFT);
        gridPaneGame.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPaneGame.setHgap(5.5);
        gridPaneGame.setVgap(5.5);

        // Place nodes in the pane
        gridPaneGame.add(new Label("Game to Insert"), 0, 0);
        gridPaneGame.add(new Label("Game Title:"), 0, 1);
        gridPaneGame.add(textFieldGameTitle, 1, 1);

        buttonInsertGame = new Button("Insert Game");
        gridPaneGame.add(buttonInsertGame, 1, 2);

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
        gridPanePlayer.setAlignment(Pos.CENTER_LEFT);
        gridPanePlayer.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPanePlayer.setHgap(5.5);
        gridPanePlayer.setVgap(5.5);

        // Place nodes in the pane
        gridPanePlayer.add(new Label("Player to Insert"), 0, 0);
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

        buttonInsertPlayer = new Button("Insert Player");
        gridPanePlayer.add(buttonInsertPlayer, 1, 7);

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
        Label label = new Label("Select which object to insert:");

        Button hiddenButton = new Button();
        hiddenButton.setPrefSize(ConnectURL.APP_WIDTH - 220, 25);
        hiddenButton.setVisible(false);

        flowPaneForRadioButtons = new FlowPane();
        flowPaneForRadioButtons.getChildren().addAll(label, hiddenButton, buttonRefresh, radioButtonPlayer,
                radioButtonGame);
    }

    private void setHandlers() {
        // Event Listeners for radio buttons.
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
        radioButtonGame.setOnAction(e ->

        {
            if (radioButtonGame.isSelected()) {
                if (gridPanePlayer != null) {
                    vBox.getChildren().remove(gridPanePlayer);
                    vBox.getChildren().add(gridPaneGame);

                } else {
                    vBox.getChildren().add(gridPaneGame);
                }
            }
        });

        buttonInsertGame.setOnAction(e -> insertGame());
        buttonInsertPlayer.setOnAction(e -> insertPlayer());

    }

    // Insert a new player to db.
    private void insertPlayer() {
        // Check if firstName and phoneNumber is null.
        if (textFieldFirstName.getText().trim().isEmpty() || textFieldPhoneNumber.getText().trim().isEmpty()) {
            new Alert(AlertType.INFORMATION, "Sorry, but First Name and Phone Number can't be empty.").show();
        } else {
            // Insert to db.
            Connection con = null;

            try {
                PreparedStatement pst;
                Class.forName(ConnectURL.DB_DRIVER);

                // establish connection to database
                con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

                pst = con.prepareStatement(
                        "Insert into [dbo].[Player] (first_name, last_name, address, postal_code, province, phone_number) VALUES(?,?,?,?,?,?)");
                // populate the fields
                pst.setString(1, textFieldFirstName.getText());
                pst.setString(2, textFieldLastName.getText());
                pst.setString(3, textFieldAddress.getText());
                pst.setString(4, textFieldPostalCode.getText());
                pst.setString(5, textFieldProvince.getText());
                pst.setString(6, textFieldPhoneNumber.getText());

                // Execute the prepared statement using executeUpdate method:
                pst.executeUpdate(); // returns the row count

                pst.close();
                new Alert(AlertType.INFORMATION, "Great! Player successfully inserted.").show();

            } catch (SQLException e) {
                e.printStackTrace();
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
    }

    // Insert the new game to db.
    private void insertGame() {
        // Check if game title is null.
        if (textFieldGameTitle.getText().trim().isEmpty()) {
            new Alert(AlertType.INFORMATION, "Sorry, but game Title can't be empty.").show();
        } else {
            // Insert to db.
            Connection con = null;
            try {
                PreparedStatement pst;

                Class.forName(ConnectURL.DB_DRIVER);

                // establish connection to database
                con = DriverManager.getConnection(ConnectURL.DB_CONNECTION_URL);

                pst = con.prepareStatement("Insert into [dbo].[Game] (game_title) VALUES(?)");
                // populate the fields
                pst.setString(1, textFieldGameTitle.getText());

                // Execute the prepared statement using executeUpdate method:
                pst.executeUpdate(); // returns the row count
                pst.close();

                new Alert(AlertType.INFORMATION, "Great! Game successfully inserted.").show();

            } catch (SQLException e) {
                e.printStackTrace();
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

    }

}
