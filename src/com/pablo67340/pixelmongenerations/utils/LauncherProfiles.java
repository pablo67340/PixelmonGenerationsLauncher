/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.auth.AuthenticationDatabase;
import com.pablo67340.pixelmongenerations.auth.SelectedUser;
import com.pablo67340.pixelmongenerations.main.MainController;
import java.io.File;
import java.io.FileReader;

import java.io.IOException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Bryce
 */
public final class LauncherProfiles {

    JSONParser parser = new JSONParser();
    AuthenticationDatabase authDatabase = new AuthenticationDatabase();
    JSONObject obj;
    SelectedUser selectedUser;
    private String userKey;
    private Boolean isFirstCreation = false;

    public LauncherProfiles() {
        try {
            obj = (JSONObject) parser.parse(new FileReader(MainController.getGameDirectory() + "/launcher_profiles.json"));
        } catch (IOException | ParseException ex) {
            isFirstCreation = true;
            createNewJSON();
        }
    }

    public void createNewJSON() {
        
        try {
            obj = (JSONObject)new JSONParser().parse("{\"settings\":{\"locale\":\"en-us\"},\"launcherVersion\":{\"name\":\"2.0.1049\",\"format\":21,\"profilesFormat\":2},\"profiles\":{},\"authenticationDatabase\":{},\"selectedUser\":{},\"analyticsToken\":\"60e9c10b5c92b0505934082de20f0041\",\"analyticsFailcount\":0}");
        } catch (ParseException ex) {
            MainController.getInstance().getLogger().getLauncherLog().warning("Error creating new launcher_profiles: "+ex.getMessage());
        }
        
        obj.put("clientToken", UUID.randomUUID().toString());
        saveLauncherProfiles();
    }

    public void load() {
        if (!isFirstCreation) {
            try{
            JSONObject userObj = (JSONObject) obj.get("selectedUser");
            selectedUser = new SelectedUser((String) userObj.get("account"), (String) userObj.get("profile"));

            JSONObject authObj = (JSONObject) obj.get("authenticationDatabase");

            for (Object key : authObj.keySet()) {
                userKey = (String) key;
                JSONObject profileObj = (JSONObject) authObj.get(userKey);
                JSONObject extraDetails = (JSONObject) profileObj.get("profiles");
                String uuid = "", displayName = "";
                for (Object subKey : extraDetails.keySet()) {
                    uuid = (String) subKey;
                    JSONObject user = (JSONObject) extraDetails.get(uuid);
                    displayName = (String) user.get("displayName");
                }
                // HOORAY! Successful mapping of JSON file! Clean this crap up later...e
                System.out.println("Building user: " + userKey + " with token: " + (String) profileObj.get("accessToken") + " with username: " + (String) profileObj.get("username") + " with uuid: " + uuid + " named: " + displayName);
                MainController.getInstance().getUsernameBox().getItems().add(displayName);

                MainController.getInstance().getUsernameBox().getSelectionModel().selectFirst();
                MainController.getInstance().getPasswordBox().setText((String) profileObj.get("accessToken"));

                authDatabase.buildUser(userKey, (String) profileObj.get("accessToken"), (String) profileObj.get("username"), uuid, displayName);
            }
            }catch(Exception e){
                MainController.getInstance().getLogger().getLauncherLog().warning("Error loading launcher_profiles.json. This should auto fix after logging in! "+e.getMessage());
            }
        }

    }

    public AuthenticationDatabase getAuthenticationDatabase() {
        return authDatabase;
    }

    public SelectedUser getSelectedUser() {
        return selectedUser;
    }

    public String getClientToken() {
        if (obj.get("clientToken") == null){
            createNewJSON();
        }
        return obj.get("clientToken").toString();
    }
    
    public String getAccessToken(String key) {
        return authDatabase.getAuthedUsers().get(key).getAccesstoken();
    }
    

    public JSONObject getJSONObject() {
        return obj;
    }

    public void setJSONObject(JSONObject input) {
        obj = input;
    }
    
    public String getUserKey(){
        return userKey;
    }
    
    public void setUserKey(String input){
        userKey = input;
    }

    public void saveLauncherProfiles() {
        try {
            FileUtils.writeStringToFile(new File(MainController.getGameDirectory() + "/launcher_profiles.json"), obj.toJSONString());

            System.out.println("Successfully Copied JSON Object to File...");

        } catch (Exception e) {
        }
    }

}
