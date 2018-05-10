/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.auth;

/**
 *
 * @author Bryce
 */
public class AuthedUser {
    
    private String accessToken, username, uuid, displayName;
    
    public AuthedUser(String token, String usr, String uid, String name){
        accessToken = token;
        username = usr;
        uuid = uid;
        displayName = name;
    }
    
    public String getAccesstoken(){
        return accessToken;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getUUID(){
        return uuid;
    }
    
    public String getDisplayName(){
        return displayName;
    }
    
    public void setAccessToken(String input){
        accessToken = input;
    }
    
    public void setUsername(String input){
        username = input;
    }
    
    public void setDisplayName(String input){
        displayName = input;
    }
    
}
