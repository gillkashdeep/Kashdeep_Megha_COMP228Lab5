package Excercise1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage myStage) {

        TabPane tabPane = new TabPane();
        tabPane.setPrefSize(ConnectURL.APP_WIDTH, ConnectURL.APP_HEIGHT);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        TabInsert tabInsert = new TabInsert();
        tabPane.getTabs().add(tabInsert);

        TabDisplay tabDisplay = new TabDisplay();
        tabPane.getTabs().add(tabDisplay);
        TabUpdate tabUpdate = new TabUpdate();
        tabPane.getTabs().add(tabUpdate);

        Scene scene = new Scene(tabPane);

        myStage.setTitle("KashdeepMegha_COMP228Lab5");
        myStage.setScene(scene);
        myStage.show();
    }

}
