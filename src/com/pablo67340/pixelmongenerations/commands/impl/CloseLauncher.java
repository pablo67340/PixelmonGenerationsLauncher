/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.commands.impl;

import com.pablo67340.pixelmongenerations.commands.Command;

/**
 *
 * @author Bryce
 */
public class CloseLauncher implements Command{

    @Override
    public void runCommand(String... args) {
        System.exit(1);
    }
    
}
