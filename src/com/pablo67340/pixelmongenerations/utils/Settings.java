/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

/**
 *
 * @author Bryce
 */
public class Settings {

    private String executable, directory, arguments, width, height;

    public Settings(String executable, String directory, String arguments, String width, String height) {
        this.executable = executable;
        this.directory = directory;
        this.arguments = arguments;
        this.width = width;
        this.height = height;
    }

    public String getExecutable() {
        return this.executable;
    }

    public void setExecutable(String input) {
        this.executable = input;
    }
    
    public String getDirectory(){
        return this.directory;
    }
    
    public void setDirectory(String input){
        this.directory = input;
    }
    
    public String getArguments(){
        return this.arguments;
    }
    
    public void setArguments(String input){
        this.arguments = input;
    }
    
    public String getWidth(){
        return this.width;
    }
    
    public void setWidth(String input){
        this.width = input;
    }
    
    public String getHeight(){
        return this.height;
    }
    
    public void setHeight(String input){
        this.height = input;
    }

}
