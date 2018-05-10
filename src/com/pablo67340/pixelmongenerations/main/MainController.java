/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;

import com.jfoenix.controls.JFXListView;
import com.pablo67340.pixelmongenerations.commands.CommandHandler;

import com.pablo67340.pixelmongenerations.utils.GameLauncher;
import com.pablo67340.pixelmongenerations.utils.LauncherProfiles;
import com.pablo67340.pixelmongenerations.utils.Log;
import com.pablo67340.pixelmongenerations.utils.Logger;
import com.pablo67340.pixelmongenerations.auth.MojangUtil;
import com.pablo67340.pixelmongenerations.utils.ResponsePrinter;
import com.pablo67340.pixelmongenerations.utils.Settings;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import java.io.File;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
    private Button btnLogin;

    @FXML
    private Tab tab1, btnProfile;

    @FXML
    private WebView webHome, webProfile;

    @FXML
    private JFXListView lstLogs;

    @FXML
    private Stage thisStage;

    @FXML
    private ComboBox txtUsername;

    @FXML
    private Label txtResponse;

    @FXML
    private TextField txtPassword, txtCommand;

    @FXML
    private TextArea txtLog;

    @FXML
    public AnchorPane anchProfile, anchMain, root;

    @FXML
    public ProgressBar prg1;

    // NON FXML VARS \\
    private Double initialX, initialY;

    private static String GameDir;

    private static MainController INSTANCE;

    private LauncherProfiles login;

    private MojangUtil mj;

    private ResponsePrinter response;

    private Logger logger;

    private Settings settings;

    private String currentLog = "Launcher";

    private Map<Integer, GameLauncher> runningGames = new HashMap<>();

    private CommandHandler commands = new CommandHandler();

    //private CleanBrowser browserCleaner = new CleanBrowser();
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String dGameDir = "";
        String OS = System.getProperty("os.name");
        String DLetter;
        String PCUser;
        File file = new File(".").getAbsoluteFile();
        File root2 = file.getParentFile();
        while (root2.getParentFile() != null) {
            root2 = root2.getParentFile();
        }
        if (OS.contains("Windows")) {
            DLetter = root2.getPath().replace("\\", "");
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
        logger = new Logger(new Log("Launcher"));
        login = new LauncherProfiles();
        login.load();
        mj = new MojangUtil();
        response = new ResponsePrinter();
        settings = new Settings();
        btnProfile.setDisable(true);

        lstLogs.getSelectionModel().select("Launcher");
        loadLog("Launcher");
        lstLogs.setOnMouseClicked((MouseEvent event) -> {
            loadLog(lstLogs.getSelectionModel().getSelectedItem().toString());
        });

        Image image = new Image(getClass().getResource("/com/pablo67340/pixelmongenerations/assets/BG.png").toExternalForm());

        BackgroundImage myBI = new BackgroundImage(image,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(myBI);

        root.setBackground(background);

        loadBrowser();
    }

    public void loadBrowser() {
        WebEngine engine = webHome.getEngine();
        engine.load("http://abstct.software/pixelmongenerations/launcher/");
        final com.sun.webkit.WebPage webPage = com.sun.javafx.webkit.Accessor.getPageFor(engine);
        webPage.setBackgroundColor(0);
        webPage.setLocalStorageEnabled(false);

        Browser browser2 = new Browser();
        BrowserView view2 = new BrowserView(browser2);
        BrowserPreferences prefs = browser2.getPreferences();
        prefs.setTransparentBackground(true);
        anchMain.getChildren().add(view2);
        view2.setPrefWidth(968);
        view2.setPrefHeight(522);
        view2.setLayoutX(6);
        browser2.loadURL("http://abstct.software/pixelmongenerations/launcher/");
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
                getLogger().getLauncherLog().log("Verifying 32 digit token!");
                if (mj.isUserValid(getPasswordBox().getText(), login.getClientToken())) {
                    btnLogin.setText("Play");
                    //Browser browser = new Browser();
                    //BrowserView view = new BrowserView(browser);

                    //anchProfile.getChildren().add(view);
                    //view.setMinSize(300, 500);
                    //view.setLayoutY(10);
                    btnProfile.setDisable(false);
                    //browser.getCacheStorage().clearCache();
                    //browser.loadURL("http://67.205.164.135/skin/");

                } else {
                    // Incorrect login
                }
            } else {
                getLogger().getLauncherLog().log("Attempting to login user...");
                if (mj.attemptLogin((String) getUsernameBox().getSelectionModel().getSelectedItem(), getPasswordBox().getText())) {
                    btnLogin.setText("Play");
                } else {
                    txtPassword.setText("");
                    getLogger().getLauncherLog().log("Login failed.");
                    // Set label to say "relogin" as the accessToken has been revoked. 
                }
            }
        } else {
            System.out.println(login.getUserKey());
            GameLauncher game = new GameLauncher(login.getUserKey(), login.getAuthenticationDatabase().getAuthedUsers().get(login.getUserKey()).getAccesstoken(), login.getAuthenticationDatabase().getAuthedUsers().get(login.getUserKey()).getDisplayName());
            game.launch();
        }
    }

    @FXML
    private void btnCommandAction(ActionEvent event) {
        commands.runCommand(txtCommand.getText());
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

    public Label getResponseLabel() {
        return txtResponse;
    }

    public ResponsePrinter getResponsePrinter() {
        return response;
    }

    public void setResponse(String input) {
        Platform.runLater(() -> {
            txtResponse.setText(input);
        });
    }

    public void disablePlayButton(Boolean input) {
        btnLogin.setDisable(input);
    }

    public Logger getLogger() {
        return logger;
    }

    public JFXListView getLstLogs() {
        return lstLogs;
    }

    public void loadLog(String input) {
        if (logger.getLogs().containsKey(currentLog)) {
            if (txtLog.getText() != null) {
                logger.getLogs().get(currentLog).setCurrentLog(txtLog.getText());
            } else {
                logger.getLogs().get(currentLog).setCurrentLog("");
            }
        }
        currentLog = input;
        txtLog.setText(logger.getLogs().get(input).getCurrentLog());

    }

    public Settings getSettings() {
        return settings;
    }

    public TextArea getLog() {
        return txtLog;
    }

    public String getCurrentLog() {
        return currentLog;
    }

    public void showProgressBar() {
        Platform.runLater(() -> {
            prg1.setVisible(true);
        });
    }

    public void hideProgressBar() {
        Platform.runLater(() -> {
            prg1.setVisible(false);
        });
    }

    public Map<Integer, GameLauncher> getRunningGames() {
        return runningGames;
    }

    public void clearCommands() {
        txtCommand.setText("");
    }

}
