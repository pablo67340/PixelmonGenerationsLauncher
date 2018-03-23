package com.pablo67340.pixelmongenerations.runnables;

import com.pablo67340.pixelmongenerations.main.MainController;
import com.pablo67340.pixelmongenerations.utils.Connections;
import com.pablo67340.pixelmongenerations.utils.Download;
import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author Bryce
 */
public final class Updater {

    protected Thread downloadUpdate;
    protected URL url;

    // END VARS \\
    public Updater() {

    }

    public static String getOnlineVersion() {

        return Connections.HTTPResponse("http://67.205.164.135/launcher/getVersion.php");

    }

    public void downloadUpdate() {
        downloadUpdate = new Thread(() -> {
            try {
                url = new URL("http://minecraftpl.us/launcher/MCPlus.jar");
                Download downloader = new Download();
                downloader.startDownload(url.toString(), MainController.getGameDirectory() + "/Launcher/Updates/", "update");
                downloadUpdate.interrupt();
            } catch (MalformedURLException ex) {
                System.out.println("Error grabbing update: " + ex.getMessage());
            }

        });
        downloadUpdate.setDaemon(true);
        downloadUpdate.start();

    }

    public void install() {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", MainController.getGameDirectory() + "/Launcher/Updates/Updater.jar");
        pb.directory(new File(MainController.getGameDirectory() + "/Launcher/Updates/"));
        try {
            Process p = pb.start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void checkUpdates(String currentVersion) {
        String source = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/";
        String destination = MainController.getGameDirectory() + "/Launcher/Updates";
        String password = "";
        File dir = new File(destination);
        dir.mkdirs();
        try {
            ZipFile zipFile = new ZipFile(source);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractFile("Updater.jar", destination);
        } catch (ZipException e) {
            System.out.println("Couldnt Extract saver!");
        }
        List<String> lines = Arrays.asList(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Path file2 = Paths.get(destination + "/location.txt");
        try {
            Files.write(file2, lines, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.out.println("Couldnt write location" + ex);
        }

        String version = getOnlineVersion();
        System.out.println("Current: " + currentVersion + " online: " + version);
        if (getOnlineVersion().equalsIgnoreCase(currentVersion)) {
            // Do nothing, All up to date!
            //MainController.getInstance().printLConsole("[Updater] Launcher is up to date!");
        } else {
            //MainController.getInstance().printLConsole("[Updater] Launcher requires an update!");
            downloadUpdate();
        }
    }
}
