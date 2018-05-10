/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.commands.impl;

import com.pablo67340.pixelmongenerations.commands.Command;
import com.pablo67340.pixelmongenerations.main.MainController;
import com.pablo67340.pixelmongenerations.utils.GameLauncher;

/**
 *
 * @author Bryce
 */
public class LaunchGame implements Command {

    @Override
    public void runCommand(String... args) {
        GameLauncher game = new GameLauncher(MainController.getInstance().getLauncherProfiles().getUserKey(), MainController.getInstance().getLauncherProfiles().getAuthenticationDatabase().getAuthedUsers().get(MainController.getInstance().getLauncherProfiles().getUserKey()).getAccesstoken(), MainController.getInstance().getLauncherProfiles().getAuthenticationDatabase().getAuthedUsers().get(MainController.getInstance().getLauncherProfiles().getUserKey()).getDisplayName());
        game.launch();
        MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("Launching Game...");

    }

}
