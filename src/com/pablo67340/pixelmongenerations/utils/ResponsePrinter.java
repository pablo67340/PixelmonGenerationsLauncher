/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import javafx.scene.paint.Color;

/**
 *
 * @author Bryce
 */
public final class ResponsePrinter {

    /**
     * Easier way to access Controller.
     */
    private final MainController controller;

    public ResponsePrinter() {
        controller = MainController.getInstance();
    }

    /**
     * Prints an error in the response label.
     */
    public void printError(String input) {
        controller.getResponseLabel().setTextFill(Color.RED);
        controller.getResponseLabel().setText(input);
        controller.getResponseLabel().setVisible(true);
    }

    /**
     * Prints green message in response label.
     */
    public void printSuccess(String input) {
        controller.getResponseLabel().setTextFill(Color.GREEN);
        controller.getResponseLabel().setText(input);
        controller.getResponseLabel().setVisible(true);
    }

    /**
     * Hides response label.
     */
    public void hideResponse() {
        controller.getResponseLabel().setVisible(false);
    }

}
