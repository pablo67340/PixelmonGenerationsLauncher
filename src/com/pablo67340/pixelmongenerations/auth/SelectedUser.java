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
public class SelectedUser {
    
    private String account, profile;
    
    public SelectedUser(String aco, String pro){
        account = aco;
        profile = pro;
    }
    
    public String getName(){
        return account;
    }
    
}
