/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.application.Platform;
import org.kamranzafar.jddl.DirectDownloader;
import org.kamranzafar.jddl.DownloadListener;
import org.kamranzafar.jddl.DownloadTask;

public final class Download implements DownloadListener {

    protected String url2;
    protected Double progress = 0.0;
    public String currentDownload = "";
    public ArrayList<String> complete = new ArrayList<>();
    public Boolean isDownloading = false;
    public DownloadTask dt;
    public String name;
    public Integer size;

    public void Download() {

    }

    public void startDownload(String url, String path, String name, DownloadListener list) {
        try {
            DirectDownloader dd = new DirectDownloader();

            url2 = url;
            String file = url2;
            String out = path + file.substring(file.lastIndexOf('/') + 1);
            dt = new DownloadTask(new URL(file), new FileOutputStream(out), list);
            dt.addListener(this);
            dd.download(dt);

            Thread t = new Thread(dd);
            t.start();

        } catch (MalformedURLException | FileNotFoundException e) {
            System.out.println("Download error: " + e.getMessage());
        }
    }

    public String getUrl() {
        return url2;
    }

    public Double getProgress() {
        Double finalProgress = RoundTo2Decimals(progress);
        System.out.println("Requested Progress: " + finalProgress);
        return finalProgress;
    }

    double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }

    public Boolean getIsDownloading() {
        return isDownloading;
    }

    @Override
    public void onStart(String string, int i) {
        name = string;
        size = i;
    }

    @Override
    public void onUpdate(int i, int i1) {
        Double progress = (double) i1 / size;
        System.out.println("Name: "+name);
        System.out.println("Progress: "+progress);
        if (name.contains("assets")) {
            Platform.runLater(() -> {
                MainController.getInstance().getProgress().get("Assets").setProgress(progress);
            });
        }
        if (name.contains("versions")) {
            Platform.runLater(() -> {
                MainController.getInstance().getProgress().get("Versions").setProgress(progress);
            });
        }
        if (name.contains("libraries")) {
            Platform.runLater(() -> {
                MainController.getInstance().getProgress().get("Libraries").setProgress(progress);
            });

        }
    }

    @Override
    public void onComplete() {
        
    }

    @Override
    public void onCancel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
