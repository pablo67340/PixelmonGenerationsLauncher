/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.commands;

import com.pablo67340.pixelmongenerations.commands.impl.ClearConsole;
import com.pablo67340.pixelmongenerations.commands.impl.CloseLauncher;
import com.pablo67340.pixelmongenerations.commands.impl.Help;
import com.pablo67340.pixelmongenerations.commands.impl.LaunchGame;
import com.pablo67340.pixelmongenerations.commands.impl.QuitGame;
import com.pablo67340.pixelmongenerations.main.MainController;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bryce
 */
public class CommandHandler {
    
    public Map<String, Command> loadedCommands = new HashMap<>();
    
    public CommandHandler(){
        loadedCommands.put("cls", new ClearConsole());
        loadedCommands.put("clear", new ClearConsole());
        loadedCommands.put("play", new LaunchGame());
        loadedCommands.put("stop", new CloseLauncher());
        loadedCommands.put("close", new CloseLauncher());
        loadedCommands.put("quit", new QuitGame());
        loadedCommands.put("help", new Help());
    }
    
    public void runCommand(String input){
        String[] cmd = input.split(" ");
        if (loadedCommands.containsKey(cmd[0])){
            loadedCommands.get(cmd[0]).runCommand(cmd);
            MainController.getInstance().clearCommands();
        }else{
            MainController.getInstance().getLog().appendText("Command not found.\n");
        }
    }
    
}
