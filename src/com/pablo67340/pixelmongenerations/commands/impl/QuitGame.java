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
public class QuitGame implements Command{

    @Override
    public void runCommand(String... args) {
        Integer id = Integer.parseInt(args[1]);
        System.out.println("ID: "+id);
        
        if (MainController.getInstance().getRunningGames().containsKey(id)){
            MainController.getInstance().getRunningGames().get(id).terminate();
            MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("Game Terminated!");

        }else{
            MainController.getInstance().getLogger().getLogs().get(MainController.getInstance().getCurrentLog()).log("Game ID not found. TIP: Usually starts at 1!");
        }
    }
    
}
