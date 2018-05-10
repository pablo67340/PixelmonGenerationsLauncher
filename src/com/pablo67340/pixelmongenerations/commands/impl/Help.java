/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.commands.impl;

import com.pablo67340.pixelmongenerations.commands.Command;
import com.pablo67340.pixelmongenerations.main.MainController;

/**
 *
 * @author Bryce
 */
public class Help implements Command {

    @Override
    public void runCommand(String... args) {
        MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("clear - Clears the current console. ALIAS: cls");
        MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("quit {id} - Force quits the game with the specified ID.");
        MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("launch - Launches a new instance of the game.");
        MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("close - Force closes the launcher.");

    }

}
