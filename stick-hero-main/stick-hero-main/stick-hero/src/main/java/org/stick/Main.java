package org.stick;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    public static Stage window;
    Scene scene;
    @FXML
    AnchorPane rootPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        window = primaryStage;

        FXMLLoaderFactory loaderFactory = new FXMLLoaderFactory();              //Used factory design pattern

        FXMLLoader endLoader = loaderFactory.createLoader("/scene2.fxml");
        FXMLLoader gameLoader = loaderFactory.createLoader("/stick.fxml");
        FXMLLoader startLoader = loaderFactory.createLoader("/start.fxml");

        Parent start = startLoader.load();
        scene = new Scene(start);
        primaryStage.setScene(scene);
        Controller controller = startLoader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }
}
