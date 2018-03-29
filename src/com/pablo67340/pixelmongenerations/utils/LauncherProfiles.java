/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Bryce
 */
public class LauncherProfiles {

    JSONParser parser = new JSONParser();
    AuthenticationDatabase authDatabase = new AuthenticationDatabase();
    JSONObject obj;
    SelectedUser selectedUser;

    public LauncherProfiles() {
        try {
            obj = (JSONObject) parser.parse(new FileReader(MainController.getGameDirectory() + "/launcher_profiles.json"));
        } catch (IOException | ParseException ex) {
            Logger.getLogger(LauncherProfiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load() {
        JSONObject userObj = (JSONObject) obj.get("selectedUser");
        selectedUser = new SelectedUser((String) userObj.get("account"), (String) userObj.get("profile"));

        JSONObject authObj = (JSONObject) obj.get("authenticationDatabase");

        for (Object key : authObj.keySet()) {
            String userKey = (String) key;
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
            MainController.getInstance().getUsernameBox().getItems().add((String) profileObj.get("username"));
            MainController.getInstance().getUsernameBox().getSelectionModel().selectFirst();
            MainController.getInstance().getPasswordBox().setText((String) profileObj.get("accessToken"));

            authDatabase.buildUser(userKey, (String) profileObj.get("accessToken"), (String) profileObj.get("username"), uuid, displayName);
        }

    }

    public AuthenticationDatabase getAuthenticationDatabase() {
        return authDatabase;
    }

    public SelectedUser getSelectedUser() {
        return selectedUser;
    }

    public String getClientToken() {
        return obj.get("clientToken").toString();
    }

    public JSONObject getJSONObject() {
        return obj;
    }

    public void setJSONObject(JSONObject input) {
        obj = input;
    }

    public void saveLauncherProfiles() {
        try {
            FileUtils.writeStringToFile(new File(MainController.getGameDirectory() + "/launcher_profiles.json"), obj.toJSONString());

            System.out.println("Successfully Copied JSON Object to File...");

        } catch (Exception e) {
        }
    }

}
