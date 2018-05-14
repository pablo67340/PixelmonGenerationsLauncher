/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.main;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXToggleButton;
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
import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.prefs.Preferences;

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
import javafx.stage.Stage;

/**
 *
 * @author Bryce
 */
public class MainController implements Initializable {

    // FXML VARS \\
    @FXML
    private Button btnLogin, btnSave;

    @FXML
    private Tab tab1, btnProfile;

    @FXML
    private JFXListView lstLogs, lstProgress, lstProgress1;

    @FXML
    private JFXToggleButton tglArguments, tglDirectory, tglExecutable, tglResolution;

    @FXML
    private Stage thisStage;

    @FXML
    private ComboBox txtUsername;

    @FXML
    private Label txtResponse, lblUsername;

    @FXML
    private TextField txtPassword, txtCommand, txtUUID, txtTrimmed, txtUrl, txtArguments, txtDirectory, txtWidth, txtHeight, txtExecutable;

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

    private Map<String, JFXProgressBar> progress = new HashMap<>();

    public Preferences userPrefs = Preferences.userNodeForPackage(this.getClass());

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
        getLogger().getLauncherLog().log("Logger initialized");
        login = new LauncherProfiles();
        getLogger().getLauncherLog().log("Loading launcher_profiles.json");
        login.load();
        getLogger().getLauncherLog().log("Initializing Mojang Utilities");
        mj = new MojangUtil();
        response = new ResponsePrinter();

        getLogger().getLauncherLog().log("ResponsePrinter & Settings loaded");
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
        getLogger().getLauncherLog().log("Loading Browser...");
        loadBrowser();

        lstProgress.setMouseTransparent(true);
        lstProgress1.setMouseTransparent(true);
        lstProgress.setFocusTraversable(false);
        lstProgress1.setFocusTraversable(false);

        loadSettings();

    }

    public void loadSettings() {
        txtDirectory.setText(getGameDirectory());
        String width = userPrefs.get("width", "");
        String height = userPrefs.get("height", "");
        String directory = userPrefs.get("directory", "");
        String executable = userPrefs.get("executable", "");
        String arguments = userPrefs.get("arguments", "");

        Boolean executable1 = Boolean.parseBoolean(userPrefs.get("executable1", "false"));

        Boolean directory1 = Boolean.parseBoolean(userPrefs.get("directory1", "false"));

        Boolean arguments1 = Boolean.parseBoolean(userPrefs.get("arguments1", "false"));

        Boolean resolution = Boolean.parseBoolean(userPrefs.get("resolution", "false"));

        if (executable.equals("")) {
            executable = txtExecutable.getText();
            userPrefs.put("executable", executable);
        }

        if (width.equals("")) {
            width = txtWidth.getText();
            userPrefs.put("width", width);
        }

        if (height.equals("")) {
            height = txtHeight.getText();
            userPrefs.put("height", height);
        }

        if (directory.equals("")) {
            directory = txtDirectory.getText();
            userPrefs.put("directory", directory);
        }

        if (arguments.equals("")) {
            arguments = txtArguments.getText();
            userPrefs.put("arguments", arguments);
        }

        userPrefs.put("executable1", executable1.toString());
        userPrefs.put("directory1", directory1.toString());
        userPrefs.put("arguments1", arguments1.toString());
        userPrefs.put("resolution", resolution.toString());

        tglExecutable.setSelected(executable1);
        txtExecutable.setDisable(!executable1);

        tglDirectory.setSelected(directory1);
        txtDirectory.setDisable(!directory1);
        tglArguments.setSelected(arguments1);
        txtArguments.setDisable(!arguments1);
        tglResolution.setSelected(resolution);
        txtWidth.setDisable(!resolution);
        txtHeight.setDisable(!resolution);
        txtWidth.setText(width);
        txtHeight.setText(height);
        settings = new Settings(executable, directory, arguments, width, height);
    }

    public void loadBrowser() {
        Browser browser2 = new Browser();
        BrowserView view2 = new BrowserView(browser2);
        BrowserPreferences prefs = browser2.getPreferences();
        prefs.setTransparentBackground(true);
        prefs.setApplicationCacheEnabled(false);
        prefs.setLocalStorageEnabled(false);
        browser2.setPreferences(prefs);
        anchMain.getChildren().add(view2);
        view2.setPrefWidth(961);
        view2.setPrefHeight(522);
        view2.setLayoutX(6);
        browser2.loadURL("http://abstct.software/pixelmongenerations/launcher/");

        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);
        BrowserPreferences prefs2 = browser.getPreferences();
        prefs2.setApplicationCacheEnabled(false);
        prefs2.setTransparentBackground(true);
        prefs2.setLocalStorageEnabled(false);
        browser.setPreferences(prefs);
        
        

        anchProfile.getChildren().add(view);
        view.setMinSize(330, 400);
        view.setLayoutY(1);
        browser.loadURL("http://abstct.software/pixelmongenerations/launcher/skin/");

        getLogger().getLauncherLog().log("Browser Loaded");
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

                    btnProfile.setDisable(false);

                } else {
                    // Incorrect login
                }
            } else {
                getLogger().getLauncherLog().log("Attempting to login user...");
                if (mj.attemptLogin((String) getUsernameBox().getSelectionModel().getSelectedItem(), getPasswordBox().getText())) {
                    getLogger().getLauncherLog().log("Login Successful!");
                    btnLogin.setText("Play");
                } else {
                    txtPassword.setText("");
                    getLogger().getLauncherLog().log("Login failed.");
                    // Set label to say "relogin" as the accessToken has been revoked. 
                }
            }
        } else {
            System.out.println(login.getUserKey());
            getLogger().getLauncherLog().log("Initializing Game");
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

    public Map<String, JFXProgressBar> getProgress() {
        return progress;
    }

    public JFXListView getProgressList() {
        return lstProgress;
    }

    public JFXListView getProgressList1() {
        return lstProgress1;
    }

    public void setProfileDetails(String username, String uuid) {
        Platform.runLater(() -> {;
            lblUsername.setText(username);
            txtUUID.setText(UUID.fromString(uuid.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            )).toString());
            String trimmed = uuid.replace("-", "");
            String url = "https://mine.ly/" + username + ".1";
            txtTrimmed.setText(trimmed);
            txtUrl.setText(url);
        });
    }

    public void toggleExecutable(ActionEvent e) {
        Boolean isEnabled = tglExecutable.isSelected();
        txtExecutable.setDisable(!isEnabled);
        userPrefs.put("executable1", isEnabled.toString());

        if (!isEnabled) {
            userPrefs.put("executable", "java");
        }
    }

    public void toggleDirectory(ActionEvent e) {
        Boolean isEnabled = tglDirectory.isSelected();
        txtDirectory.setDisable(!isEnabled);
        userPrefs.put("directory1", isEnabled.toString());

        if (!isEnabled) {
            userPrefs.put("directory", MainController.getGameDirectory());
        }
    }

    public void toggleArguments(ActionEvent e) {
        Boolean isEnabled = tglArguments.isSelected();
        txtArguments.setDisable(!isEnabled);
        userPrefs.put("arguments1", isEnabled.toString());

        if (!isEnabled) {
            userPrefs.put("arguments", "!Xmx2G !Xms2G");
        }
    }

    public void toggleResolution(ActionEvent e) {
        Boolean isEnabled = tglResolution.isSelected();
        txtWidth.setDisable(!isEnabled);
        txtHeight.setDisable(!isEnabled);
        userPrefs.put("resolution", isEnabled.toString());

        if (!isEnabled) {
            updateLine("854", "480");
            userPrefs.put("width", "854");
            userPrefs.put("height", "480");
            txtWidth.setText("854");
            txtHeight.setText("480");
        }
    }

    public void btnSaveAction(ActionEvent e) {
        userPrefs.put("executable", txtExecutable.getText());
        userPrefs.put("directory", txtDirectory.getText());
        userPrefs.put("width", txtWidth.getText());
        userPrefs.put("height", txtHeight.getText());
        userPrefs.put("arguments", txtArguments.getText());
        settings.setExecutable(txtExecutable.getText());
        settings.setDirectory(txtDirectory.getText());
        settings.setWidth(txtWidth.getText());
        settings.setHeight(txtHeight.getText());
        settings.setArguments(txtArguments.getText());

        updateLine(txtWidth.getText(), txtHeight.getText());

    }

    private void updateLine(String wdth, String hght) {

        try {
            File data = new File(MainController.getGameDirectory() + "/options.txt");
            if (data.exists()) {
                BufferedReader file = new BufferedReader(new FileReader(data));
                String line;
                String input = "";
                String width = "", height = "";
                while ((line = file.readLine()) != null) {
                    if (line.contains("overrideWidth:")) {
                        width = line;
                    }
                    if (line.contains("overrideHeight:")) {
                        height = line;
                    }
                    input += line + "\n";
                }

                input = input.replace(width, "overrideWidth:" + wdth);

                input = input.replace(height, "overrideHeight:" + hght);

                FileOutputStream os = new FileOutputStream(data);
                os.write(input.getBytes());

                file.close();
                os.close();
            }
        } catch (Exception e) {

        }
    }
}
