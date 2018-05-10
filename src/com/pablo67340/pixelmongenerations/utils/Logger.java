/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bryce
 */
public class Logger {

    /**
     * Easier way to access controller.
     */
    private final MainController controller;

    /**
     * The map of logs being used.
     */
    private final Map<String, Log> logs;

    public Logger() {
        controller = MainController.getInstance();
        logs = new HashMap<>();
    }

    public Logger(Log input) {
        controller = MainController.getInstance();
        logs = new HashMap<>();
        logs.put(input.getLogName(), input);
        controller.getLstLogs().getItems().add(input.getLogName());
    }

    /**
     * Adds log to cache
     */
    public void attachLog(Log log) {
        getLogs().put(log.getLogName(), log);
        getController().getLstLogs().getItems().add(log.getLogName());
    }

    /**
     * Removes log from cache.
     */
    public void detachLog(String name) {
        getLogs().remove(name);
        getController().getLstLogs().getItems().remove(name);
    }

    /**
     * Gets the map of current logs.
     */
    public Map<String, Log> getLogs() {
        return logs;
    }

    /**
     * Easier way to access controller.
     *
     * @return Controller
     */
    public MainController getController() {
        return controller;
    }

    /**
     * Gets the Launcher log object.
     *
     * @return log
     */
    public Log getLauncherLog() {
        return logs.get("Launcher");
    }

}
