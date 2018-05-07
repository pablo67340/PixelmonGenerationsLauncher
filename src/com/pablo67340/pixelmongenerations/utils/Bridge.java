/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import javafx.application.Platform;

/**
 *
 * @author Bryce
 */
public class Bridge {

    public void exit() {
        Platform.exit();
    }

    public void log(String text) {
        System.out.println(text);
    }

}
