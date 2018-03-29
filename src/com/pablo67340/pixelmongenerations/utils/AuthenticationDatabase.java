/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bryce
 */
public class AuthenticationDatabase {
    
    private Map<String, AuthedUser> authedUsers = new HashMap<>();
    
    
    public AuthenticationDatabase(){
        
    }
    
    public void buildUser(String randToken, String accessToken, String username, String UUID, String displayName){
        AuthedUser newUser = new AuthedUser(accessToken, username, UUID, displayName);
        authedUsers.put(randToken, newUser);
    }
    
    public Map<String, AuthedUser> getAuthedUsers(){
        return authedUsers;
    }
    
    
}
