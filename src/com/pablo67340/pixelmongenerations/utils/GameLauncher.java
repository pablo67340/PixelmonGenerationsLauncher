/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.kamranzafar.jddl.DownloadListener;

/**
 *
 * @author Bryce
 */
public class GameLauncher implements DownloadListener {

    private final String client, session, username;

    private final MainController controller;

    private final Log log;

    private Integer fileIntegrity = 0;

    private Integer counter = 1, unzipsComplete = 0;

    private ProcessBuilder ps;
    private Process pr;
    private BufferedReader in;
    private String line;

    private Timeline Tassets, Tlibs, Tversions;

    public GameLauncher(String client, String session, String username) {
        this.client = client;
        this.session = session;
        this.username = username;
        controller = MainController.getInstance();
        if (!controller.getLogger().getLogs().isEmpty()) {
            counter = controller.getLogger().getLogs().keySet().stream().filter((str) -> (str.contains("Game"))).map((_item) -> 1).reduce(counter, Integer::sum);
        }
        log = new Log("Game " + counter);
        controller.getLogger().attachLog(log);

    }

    private String getclient() {
        return client;
    }

    private String getSession() {
        return session;
    }

    private String getUsername() {
        return username;
    }

    public Boolean isEverythingInPlace() {
        return false;
    }

    public void downloadLibraries() {
        Download download = new Download();
        download.startDownload("http://abstct.software/pixelmongenerations/launcher/libraries.zip", MainController.getGameDirectory() + "/", "libraries", this);
    }

    public void downloadAssets() {
        Download download = new Download();
        download.startDownload("http://abstct.software/pixelmongenerations/launcher/assets.zip", MainController.getGameDirectory() + "/", "assets", this);
    }

    public void downloadVersions() {
        Download download = new Download();
        download.startDownload("http://abstct.software/pixelmongenerations/launcher/versions.zip", MainController.getGameDirectory() + "/", "versions", this);
    }

    public Boolean checkStage() {

        fileIntegrity = 0;

        File libraries = new File(MainController.getGameDirectory() + "/libraries.zip");
        File assets = new File(MainController.getGameDirectory() + "/assets.zip");
        File versions = new File(MainController.getGameDirectory() + "/versions.zip");

        if (!libraries.exists()) {
            downloadLibraries();
            getController().showProgressBar();
            getController().getResponsePrinter().printSuccess("Downloading...");
        } else {
            fileIntegrity += 1;
        }

        if (!assets.exists()) {
            downloadAssets();
            getController().showProgressBar();
            getController().getResponsePrinter().printSuccess("Downloading...");
        } else {
            fileIntegrity += 1;
        }

        if (!versions.exists()) {
            downloadVersions();
            getController().showProgressBar();
            getController().getResponsePrinter().printSuccess("Downloading...");
        } else {
            fileIntegrity += 1;
        }

        return fileIntegrity == 3;

    }

    @Override
    public void onStart(String string, int i) {

    }

    @Override
    public void onUpdate(int i, int i1) {

    }

    public void unzipAssets() {
        ZipFile assets = null;

        try {
            assets = new ZipFile(MainController.getGameDirectory() + "/assets.zip");
            assets.extractAll(MainController.getGameDirectory() + "/");

        } catch (ZipException ex) {
            Logger.getLogger(GameLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (assets != null) {
            ProgressMonitor progressMonitor = assets.getProgressMonitor();
            Tassets = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
                System.out.println("Waiting for assets...");
                if (progressMonitor.getPercentDone() == 100) {
                    progressMonitor.cancelAllTasks();
                    unzipsComplete += 1;

                    if (unzipsComplete == 3) {
                        launch();
                    }
                    Tassets.stop();
                }
            }));
            Tassets.setCycleCount(Animation.INDEFINITE);
            Tassets.play();

        }
    }

    public void unzipLibs() {
        ZipFile libs = null;

        try {
            libs = new ZipFile(MainController.getGameDirectory() + "/libraries.zip");
            libs.extractAll(MainController.getGameDirectory() + "/");

        } catch (ZipException ex) {
            Logger.getLogger(GameLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (libs != null) {
            ProgressMonitor progressMonitor = libs.getProgressMonitor();
            Tlibs = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
                System.out.println("Waiting for libs...");
                if (progressMonitor.getPercentDone() == 100) {
                    progressMonitor.cancelAllTasks();
                    unzipsComplete += 1;

                    if (unzipsComplete == 3) {
                        launch();
                    }
                    Tlibs.stop();
                }
            }));
            Tlibs.setCycleCount(Animation.INDEFINITE);
            Tlibs.play();

        }
    }

    public void unzipVersions() {
        ZipFile versions = null;

        try {
            versions = new ZipFile(MainController.getGameDirectory() + "/versions.zip");
            versions.extractAll(MainController.getGameDirectory() + "/");

        } catch (ZipException ex) {
            Logger.getLogger(GameLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (versions != null) {
            ProgressMonitor progressMonitor = versions.getProgressMonitor();
            Tversions = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
                System.out.println("Waiting for versions...");
                if (progressMonitor.getPercentDone() == 100) {
                    progressMonitor.cancelAllTasks();
                    unzipsComplete += 1;

                    if (unzipsComplete == 3) {
                        launch();
                        System.out.println("Launching!");
                    }
                    Tversions.stop();
                }
            }));
            Tversions.setCycleCount(Animation.INDEFINITE);
            Tversions.play();
        }
    }

    public void unzip() {

        getController().getResponsePrinter().printSuccess("Unpacking...");

        unzipAssets();

        unzipLibs();

        unzipVersions();

    }

    @Override
    public void onComplete() {
        fileIntegrity += 1;
        if (fileIntegrity == 3) {
            System.out.println("Downloads complete! UNZIP");
            Thread t1 = new Thread(() -> {
                unzip();
            });
            t1.start();

        }
    }

    @Override
    public void onCancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String buildCommandLine() {
        String commandLine = "";
        try {
            URL url = new URL("http://abstct.software/pixelmongenerations/launcher/cmd-windows.txt");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {

                System.out.println(line);
                commandLine += line + " ";

            }

            in.close();

        } catch (IOException ex) {
            ex.printStackTrace(); // for now, simply output it.
        }

        commandLine = commandLine.replace("+ EXEC +", getController().getSettings().getExecutable());
        commandLine = commandLine.replace("+ username +", getController().getLauncherProfiles().getAuthenticationDatabase().getAuthedUsers().get(getController().getLauncherProfiles().getUserKey()).getDisplayName());
        commandLine = commandLine.replace("+ uuid +", getController().getLauncherProfiles().getAuthenticationDatabase().getAuthedUsers().get(getController().getLauncherProfiles().getUserKey()).getUUID());
        commandLine = commandLine.replace("+ session +", getController().getLauncherProfiles().getAuthenticationDatabase().getAuthedUsers().get(getController().getLauncherProfiles().getUserKey()).getAccesstoken());
        commandLine = commandLine.replace("+ PCUser +", System.getProperty("user.name"));

        System.out.println("built: " + commandLine);
        return commandLine;
    }

    public void launch() {
        if (checkStage()) {
            getController().hideProgressBar();
            getController().getResponsePrinter().printSuccess("Playing");
            getController().disablePlayButton(true);
            Thread launch = new Thread(() -> {
                try {
                    ps = new ProcessBuilder("CMD.exe", "/K", buildCommandLine());
                    ps.redirectErrorStream(true);

                    pr = ps.start();

                    getController().getRunningGames().put(counter, this);

                    in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    while ((line = in.readLine()) != null) {
                        if (getController().getLogger().getLogs().containsKey("Game " + counter)) {
                            getController().getLogger().getLogs().get("Game " + counter).log(line);
                        }
                        if (line.contains("Stopping!") || line.contains("Exit trace")) {
                            pr.destroy();

                            Platform.runLater(() -> {
                                onClose();
                            });

                        }

                    }
                    try {
                        pr.waitFor();
                    } catch (InterruptedException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }

                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            });
            launch.start();
        } else {
            System.out.println("checkStage failed");
        }

    }

    public Process getProcess() {
        return pr;
    }

    public void onClose() {
        getController().getLogger().detachLog("Game " + counter);
        getController().loadLog("Launcher");
        getController().disablePlayButton(false);
        getController().getResponsePrinter().printSuccess("Waiting");

    }

    public MainController getController() {
        return controller;
    }
    
    public void terminate(){
         Thread launch = new Thread(() -> {
                try {
                    ps = new ProcessBuilder("CMD.exe", "/K", "taskkill /f /fi \"WINDOWTITLE eq Minecraft*");
                    ps.redirectErrorStream(true);

                    pr = ps.start();

                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            });
            launch.start();
            onClose();
    }

}
