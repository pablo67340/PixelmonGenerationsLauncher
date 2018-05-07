/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.teamdev.jxbrowser.chromium.Browser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bryce
 */
public final class CleanBrowser {
    
    private static List<Browser> browsers;
    
    public CleanBrowser(){
        browsers = new ArrayList<>();
    }
    
    public List<Browser> getBrowsers(){
        return browsers;
    }
    
    public void addBrowser(Browser input){
        browsers.add(input);
    }
    
    
}
