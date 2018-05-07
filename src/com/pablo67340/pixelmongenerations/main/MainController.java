/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;


import com.pablo67340.pixelmongenerations.utils.LauncherProfiles;
import com.pablo67340.pixelmongenerations.utils.MojangUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Bryce
 */
public class MainController implements Initializable {

    // FXML VARS \\
    @FXML
    private Button btnClose, btnMinimize, btnLogin;

    @FXML
    private Tab tab1, btnProfile;

    @FXML
    private WebView webHome, webProfile;

    @FXML
    private Stage thisStage;

    @FXML
    private ComboBox txtUsername;
    
    @FXML
    private TextField txtPassword;

    @FXML
    public AnchorPane anchProfile, anchMain;

    // NON FXML VARS \\
    private Double initialX, initialY;

    private static String GameDir;

    private static MainController INSTANCE;

    private String processID;

    private LauncherProfiles login;

    private MojangUtil mj;

    //private CleanBrowser browserCleaner = new CleanBrowser();
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        String dGameDir = "";
        String OS = System.getProperty("os.name");
        String DLetter;
        String PCUser;
        File file = new File(".").getAbsoluteFile();
        File root = file.getParentFile();
        while (root.getParentFile() != null) {
            root = root.getParentFile();
        }
        if (OS.contains("Windows")) {
            DLetter = root.getPath().replace("\\", "");
            PCUser = System.getProperty("user.name");
            dGameDir = DLetter + "/Users/" + PCUser + "/AppData/Roaming/.minecraft";
        } else if (OS.contains("Linux")) {
            PCUser = System.getProperty("user.name");
            dGameDir = "/home/" + PCUser + "/.minecraft";
        } else if (OS.contains("Mac")) {

            dGameDir = "~/Library/Application Support/minecraft";
        }
        GameDir = dGameDir;
        INSTANCE = this;
        login = new LauncherProfiles();
        login.load();
        mj = new MojangUtil();
        btnProfile.setDisable(true);
        loadBrowser();
    }

    public void loadBrowser() {
        Browser browser2 = new Browser();
        BrowserView view2 = new BrowserView(browser2);
        anchMain.getChildren().add(view2);
        view2.setPrefWidth(968);
        view2.setPrefHeight(522);
        view2.setLayoutX(6);
        browser2.loadURL("http://67.205.164.135/");
    }

    public void setStage(Stage stage) {
        this.thisStage = stage;

    }

    public Stage getStage() {
        return thisStage;
    }

    public ComboBox getUsernameBox() {
        return txtUsername;
    }

    public TextField getPasswordBox() {
        return txtPassword;
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
    private void btnLoginAction(ActionEvent event) {
        if (btnLogin.getText().equalsIgnoreCase("Login")) {
            if (getPasswordBox().getText().length() == 32) {
                if (mj.isUserValid(getPasswordBox().getText(), login.getClientToken())) {
                    btnLogin.setText("Play");
                    Browser browser = new Browser();
                    BrowserView view = new BrowserView(browser);

                    anchProfile.getChildren().add(view);
                    view.setMinSize(300, 500);
                    view.setLayoutY(10);
                    btnProfile.setDisable(false);
                    browser.getCacheStorage().clearCache();
                    browser.loadURL("http://67.205.164.135/skin/");
                    //Main.getInstance().getBrowserCleaner().addBrowser(browser);

                } else {
                    // Incorrect login
                }
            } else {
                if (mj.attemptLogin((String) getUsernameBox().getSelectionModel().getSelectedItem(), getPasswordBox().getText())) {
                    btnLogin.setText("Play");
                } else {
                    txtPassword.setText("");
                    // Set label to say "relogin" as the accessToken has been revoked. 
                }
            }
        } else {
            // Play the game here!
        }
    }

    @FXML
    private void btnReloadAction(ActionEvent event) {

    }

    @FXML
    private void btnCloseAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void btnMinimizeAction(ActionEvent event) {
        getStage().setIconified(true);
    }

    @FXML
    private void txtUsernameAction(ActionEvent e) {
        getPasswordBox().setText("");
    }

    public void mouseDragAction(MouseEvent e) {
        getStage().getScene().getWindow().setX(e.getScreenX() - initialX);
        getStage().getScene().getWindow().setY(e.getScreenY() - initialY);
    }

    public void dragAction(MouseEvent event) {
        this.initialX = event.getSceneX();
        this.initialY = event.getSceneY();
    }

    public static MainController getInstance() {
        return INSTANCE;
    }

    public static String getGameDirectory() {
        return GameDir;
    }

    public LauncherProfiles getLauncherProfiles() {
        return login;
    }

}
