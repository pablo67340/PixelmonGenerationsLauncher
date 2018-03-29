/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;

import com.pablo67340.pixelmongenerations.utils.LauncherProfiles;
import com.pablo67340.pixelmongenerations.utils.MojangUtil;
import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.scene.web.WebEngine;
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
    private Tab tab1;

    @FXML
    private WebView webHome;

    @FXML
    private Stage thisStage;

    @FXML
    private ComboBox txtUsername;
    @FXML
    private TextField txtPassword;

    // NON FXML VARS \\
    private Double initialX, initialY;

    private static String GameDir;

    private static MainController INSTANCE;

    private String processID;

    private LauncherProfiles login;

    private MojangUtil mj;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WebEngine webEngine = webHome.getEngine();
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.44 (KHTML, like Gecko) Chrome/8.0 JavaFX/8.0 Safari/537.44");
        webEngine.load("http://67.205.164.135/");
        webEngine.getHistory().setMaxSize(0);
        java.net.CookieHandler.setDefault(new java.net.CookieManager());
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
                if (mj.isUserValid(getPasswordBox().getText(), login.getClientToken())){
                    btnLogin.setText("Play");
                }else{
                    // Incorrect login
                }
            } else {
                if (mj.attemptLogin((String) getUsernameBox().getSelectionModel().getSelectedItem(), getPasswordBox().getText())) {
                    btnLogin.setText("Play");
                }else{
                    // Incorrect login
                }
            }
        } else {
            // Play the game here!
        }
    }

    @FXML
    private void btnCloseAction(ActionEvent event) {
        System.exit(1);
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
