/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;

import com.pablo67340.pixelmongenerations.runnables.Updater;
import com.pablo67340.pixelmongenerations.utils.CleanBrowser;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Bryce
 */
public class Main extends Application {

    private MainController controller;

    private Stage thisStage;
    
    private static CleanBrowser browserCleaner = new CleanBrowser();
   
    
   
    
    @Override
    public void init() throws Exception {
        // On Mac OS X Chromium engine must be initialized in non-UI thread.
            //BrowserCore.initialize();
        
    }

    @Override
    public void start(Stage stage) throws Exception {
        thisStage = stage;
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
        stage.setTitle("PixelmonGenerations v1.3");
        stage.getIcons().add(new Image(getClass().getResource("/com/pablo67340/pixelmongenerations/assets/logo_original.png").toExternalForm()));

        controller.setStage(stage);
        controller.showStage();
        Updater updater = new Updater();
        updater.checkUpdates("1.3");
        //controller.updateUI();
        Platform.setImplicitExit(false);
 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        thisStage.hide();
      
        //browserCleaner.getBrowsers().forEach((browser) -> {
           // browser.dispose();
       // });
        
    }
    
    public static CleanBrowser getBrowserCleaner(){
        return browserCleaner;
    }
   
    
 
            

}
