/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Bryce
 */
public class MainController implements Initializable {

    @FXML
    private Button btnClose, btnMinimize;
    
    @FXML
    private Tab tab1;

    @FXML
    private WebView webHome;

    @FXML
    private Stage thisStage;
    
    
    // NON FXML VARS
    private Double initialX, initialY;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WebEngine webEngine = webHome.getEngine();
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.44 (KHTML, like Gecko) Chrome/8.0 JavaFX/8.0 Safari/537.44");
        webEngine.load("http://67.205.164.135/");
        webEngine.getHistory().setMaxSize(0);
        java.net.CookieHandler.setDefault(new java.net.CookieManager());
    }

    public void setStage(Stage stage) {
        this.thisStage = stage;
    }
    
    public Stage getStage(){
        return thisStage;
    }

    public void showStage() {
        getStage().show();
        //txtGameLog.textProperty().addListener((ObservableValue<?> observable, Object oldValue, Object newValue) -> {
        //  txtGameLog.setScrollTop(Double.MAX_VALUE);
        //});

        //txtLauncherLog.textProperty().addListener((ObservableValue<?> observable, Object oldValue, Object newValue) -> {
        // txtLauncherLog.setScrollTop(Double.MAX_VALUE);
        // });
    }
    
    @FXML
    private void btnCloseAction(ActionEvent event){
        System.exit(1);
    }
    
    @FXML
    private void btnMinimizeAction(ActionEvent event){
        getStage().setIconified(true);
    }

    public void mouseDragAction(MouseEvent e) {
        getStage().getScene().getWindow().setX(e.getScreenX() - initialX);
        getStage().getScene().getWindow().setY(e.getScreenY() - initialY);
    }

    public void dragAction(MouseEvent event) {
        this.initialX = event.getSceneX();
        this.initialY = event.getSceneY();
    }
}
