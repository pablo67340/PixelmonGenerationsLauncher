package com.pablo67340.pixelmongenerations.runnables;

import com.pablo67340.pixelmongenerations.main.MainController;
import com.pablo67340.pixelmongenerations.utils.Connections;
import com.pablo67340.pixelmongenerations.utils.Download;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang.StringUtils;

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
                url = new URL("http://67.205.164.135/launcher/Launcher.jar");
                Download downloader = new Download();
                downloader.startDownload(url.toString(), MainController.getGameDirectory() + "/Launcher/Updates/", "update");

            } catch (MalformedURLException ex) {
                System.out.println("Error grabbing update: " + ex.getMessage());
            }

        });
        downloadUpdate.setDaemon(true);
        downloadUpdate.start();

        Thread installer = new Thread(() -> {
            install();
        });

        try {
            downloadUpdate.join(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
        installer.start();

    }

    public void install() {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", MainController.getGameDirectory() + "/Launcher/Updates/Updater.jar");
        pb.directory(new File(MainController.getGameDirectory() + "/Launcher/Updates/"));
        try {
            Process p = pb.start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.exit(1);
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
        List<String> lines = Arrays.asList(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), StringUtils.substringBefore(ManagementFactory.getRuntimeMXBean().getName(), "@"));
        Path file2 = Paths.get(destination + "/location.txt");
        try {
            Files.write(file2, lines, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.out.println("Couldnt write location" + ex);
        }

        String version = getOnlineVersion();
        System.out.println("Current: " + currentVersion + " online: " + version);
        if (version.equalsIgnoreCase(currentVersion)) {
            // Do nothing, All up to date!
            //MainController.getInstance().printLConsole("[Updater] Launcher is up to date!");
        } else {
            //MainController.getInstance().printLConsole("[Updater] Launcher requires an update!");
            downloadUpdate();
        }
    }
}
