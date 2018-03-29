/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import com.pablo67340.pixelmongenerations.main.MainController;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Bryce
 */
public class MojangUtil {

    private String loginResponse;

    public MojangUtil() {

    }

    public Boolean isUserValid(String accessToken, String clientToken) {
        JSONObject obj = new JSONObject();
        obj.put("accessToken", accessToken);
        obj.put("clientToken", clientToken);
        String response = "";
        try {
            response = Connections.HTTPValidateResponse(new URL("https://authserver.mojang.com/validate"), obj.toJSONString());
            System.out.println("Response: " + response);
            return true;
        } catch (MalformedURLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    public Boolean attemptLogin(String username, String password) {
        JSONObject agent = new JSONObject();
        JSONObject obj = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", 1);
        obj.put("agent", agent);
        obj.put("username", username);
        obj.put("password", password);
        obj.put("clientToken", MainController.getInstance().getLauncherProfiles().getClientToken());
        obj.put("requestUser", false);

        System.out.println("Payload" + obj.toJSONString());

        // spacer
        String response = "";
        try {
            response = Connections.HTTPPostResponse(new URL("https://authserver.mojang.com/authenticate"), obj.toJSONString());
            System.out.println("Response: " + response);
            loginResponse = response;
            updateLauncherProfiles();
            return true;
        } catch (MalformedURLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            Logger.getLogger(MojangUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updateLauncherProfiles() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(getLoginResponse());
            JSONObject obj2 = MainController.getInstance().getLauncherProfiles().getJSONObject();
            JSONObject selectedProfile = (JSONObject) obj.get("selectedProfile");
            String accessToken = (String) obj.get("accessToken");
            String clientToken = (String) obj.get("clientToken");
            String uuid = (String) selectedProfile.get("id");
            String displayName = (String) selectedProfile.get("name");

            for (Entry<String, AuthedUser> entry : MainController.getInstance().getLauncherProfiles().getAuthenticationDatabase().getAuthedUsers().entrySet()) {
                String key = entry.getKey();
                AuthedUser user = entry.getValue();
                if (user.getUUID().equalsIgnoreCase(uuid)) {
                    System.out.println("key: " + key + " EQUAL UUID's");
                    user.setAccessToken(accessToken);
                    user.setDisplayName(key);

                    JSONObject authdb = (JSONObject) obj2.get("authenticationDatabase");

                    System.out.println("key: " + key);
                    JSONObject profile = (JSONObject) authdb.get(key);
                    profile.put("accessToken", accessToken);

                    JSONObject profiles = (JSONObject) profile.get("profiles");
                    JSONObject uid = (JSONObject) profiles.get(uuid);
                    uid.put("displayName", displayName);
                    System.out.println("final updated: " + obj2.toJSONString());
                    MainController.getInstance().getLauncherProfiles().setJSONObject(obj2);
                    MainController.getInstance().getLauncherProfiles().saveLauncherProfiles();
                } else {
                    System.out.println("Searching ID: " + key + " NOT EQUAL");
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(MojangUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLoginResponse() {
        return loginResponse;
    }


}
