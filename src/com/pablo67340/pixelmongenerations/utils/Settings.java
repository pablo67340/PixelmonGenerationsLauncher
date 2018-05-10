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
    
    private String exec;
    
    public Settings(){
        exec = "java";
    }
    
    public String getExecutable(){
        return exec;
    }
    
    public void setExecutable(String input){
        exec = input;
    }
    
    
    
}
