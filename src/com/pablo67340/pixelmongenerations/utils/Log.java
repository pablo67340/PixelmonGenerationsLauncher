/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import java.util.Date;
import java.util.logging.Level;
import javafx.application.Platform;

/**
 *
 * @author Bryce
 */
public class Log {

    /**
     * The name of the Log.
     */
    private final String logName;

    /**
     * The current cached log.
     */
    private String currentLog;

    public Log(String name) {
        logName = name;
        currentLog = "";

    }

    /**
     * Gets the log name
     *
     * @return Log name
     */
    public String getLogName() {
        return logName;
    }

    /**
     * Automatically surrounds string with brackets.
     */
    public String tagify(String input) {
        return "[" + input + "]";
    }

    /**
     * Logs to the console.
     */
    public void log(String input) {
        Platform.runLater(() -> {
            if (MainController.getInstance().getCurrentLog().equals(logName)) {
                MainController.getInstance().getLog().appendText(tagify(getLogName()) + tagify("INFO") + tagify(getCurrentTime()) + " - " + input + "\n");
            } else {
                currentLog += input + "\n";
            }
        });

    }

    /**
     * Logs to the console. Optional level
     */
    public void log(String input, Level level) {
        Platform.runLater(() -> {
            if (MainController.getInstance().getCurrentLog().equals(logName)) {
                MainController.getInstance().getLog().appendText(tagify(getLogName()) + tagify(level.getName()) + tagify(getCurrentTime()) + " - " + input + "\n");
            } else {
                currentLog += input + "\n";
            }
        });

    }

    /**
     * Log to console. Optional level
     */
    public void log(String input, String level) {
        Platform.runLater(() -> {
            if (MainController.getInstance().getCurrentLog().equals(logName)) {
                MainController.getInstance().getLog().appendText(tagify(getLogName()) + tagify(level) + tagify(getCurrentTime()) + " - " + input + "\n");
            } else {
                currentLog += input + "\n";
            }
        });
    }

    /**
     * Log to console. Used for errors.
     */
    public void warning(String input) {
        Platform.runLater(() -> {
            if (MainController.getInstance().getCurrentLog().equals(logName)) {
                MainController.getInstance().getLog().appendText(tagify(getLogName()) + tagify("WARNING") + tagify(getCurrentTime()) + " - " + input + "\n");
            } else {
                currentLog += input + "\n";
            }
        });
    }

    /**
     * Gets the current date & time.
     */
    public String getCurrentTime() {
        return new Date(System.currentTimeMillis()).toString();
    }

    /**
     * Gets the current cached log.
     *
     * @return current log
     */
    public String getCurrentLog() {
        return currentLog;
    }

    /**
     * Sets the cached log.
     */
    public void setCurrentLog(String input) {
        currentLog = input;
    }
}
