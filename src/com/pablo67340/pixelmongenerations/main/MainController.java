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

import javafx.scene.control.Tab;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Bryce
 */
public class MainController implements Initializable {

    @FXML
    public Tab tab1;

    @FXML
    public WebView webHome;

    @FXML
    public Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WebEngine webEngine = webHome.getEngine();
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.44 (KHTML, like Gecko) Chrome/8.0 JavaFX/8.0 Safari/537.44");
        webEngine.load("http://67.205.164.135/");
        webEngine.getHistory().setMaxSize(0);
        java.net.CookieHandler.setDefault(new java.net.CookieManager());
    }

    public void setStage(Stage stage) {
        thisStage = stage;
    }

    public void showStage() {
        thisStage.show();
        //txtGameLog.textProperty().addListener((ObservableValue<?> observable, Object oldValue, Object newValue) -> {
        //  txtGameLog.setScrollTop(Double.MAX_VALUE);
        //});

        //txtLauncherLog.textProperty().addListener((ObservableValue<?> observable, Object oldValue, Object newValue) -> {
        // txtLauncherLog.setScrollTop(Double.MAX_VALUE);
        // });
    }
}
