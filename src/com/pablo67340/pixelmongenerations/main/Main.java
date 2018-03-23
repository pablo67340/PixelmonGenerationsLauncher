/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;

import com.pablo67340.pixelmongenerations.runnables.Updater;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Bryce
 */
public class Main extends Application {

    private MainController controller;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("OS.NAME: " + System.getProperty("os.name"));
        URL location = getClass().getResource("Main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        controller = fxmlLoader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("PixelmonGenerations v1.0");
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/img/icon.png")));

        controller.setStage(stage);
        controller.showStage();
        Updater updater = new Updater();
        updater.checkUpdates("1.0");
        //controller.updateUI();
        Platform.setImplicitExit(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
