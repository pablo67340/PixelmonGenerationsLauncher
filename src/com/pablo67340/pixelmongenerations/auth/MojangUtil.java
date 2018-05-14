/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.auth;

import com.pablo67340.pixelmongenerations.main.MainController;
import com.pablo67340.pixelmongenerations.utils.Connections;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.UUID;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Bryce
 */
public class MojangUtil {

    private String loginResponse;

    private final MainController controller;

    private String currentSession;

    public MojangUtil() {
        controller = MainController.getInstance();
    }

    public Boolean isUserValid(String accessToken, String clientToken) {
        JSONObject obj = new JSONObject();
        obj.put("accessToken", accessToken);
        obj.put("clientToken", clientToken);
        String response = "";
        try {
            response = Connections.HTTPValidateResponse(new URL("https://authserver.mojang.com/validate"), obj.toJSONString());

            if (!response.contains("Invalid token")) {
                getController().getResponsePrinter().printSuccess("Challenge Success!");
                getController().getLogger().getLauncherLog().log("A user has been logged in!");
                return true;
            } else {
                getController().getResponsePrinter().printError("Invalid Session. Please re-login.");
                MainController.getInstance().getPasswordBox().clear();
                return false;

            }

        } catch (MalformedURLException ex) {
            getController().getLogger().getLauncherLog().warning("Error checking user validity: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            getController().getLogger().getLauncherLog().warning("Error checking user validity: " + ex.getMessage());
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

        // spacer
        String response = "";
        try {
            response = Connections.HTTPPostResponse(new URL("https://authserver.mojang.com/authenticate"), obj.toJSONString());
            loginResponse = response;
            System.out.println(loginResponse);
            if (!loginResponse.contains("Invalid")) {
                JSONParser parser = new JSONParser();
                JSONObject responseObj = (JSONObject) parser.parse(response);
                String accessToken = (String) responseObj.get("accessToken");
                JSONObject selectedProfile = (JSONObject) responseObj.get("selectedProfile");
                String uuid = (String) selectedProfile.get("id");
                String name = (String) selectedProfile.get("name");
                String random = UUID.randomUUID().toString();
                MainController.getInstance().getLauncherProfiles().setUserKey(random);
                MainController.getInstance().getLauncherProfiles().getAuthenticationDatabase().buildUser(random, accessToken, (String) MainController.getInstance().getUsernameBox().getSelectionModel().getSelectedItem(), uuid, name);
                MainController.getInstance().setProfileDetails(name, uuid);
                updateLauncherProfiles();
                MainController.getInstance().getLauncherProfiles().saveLauncherProfiles();
                getController().getResponsePrinter().printSuccess("Challenge Success!");
            } else {
                return false;
            }
            return true;

        } catch (MalformedURLException ex) {
            getController().getLogger().getLauncherLog().warning("Error attempting to login: " + ex.getMessage());
            return false;
        } catch (ParseException ex) {
            getController().getLogger().getLauncherLog().warning("Error attempting to login: " + ex.getMessage());
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

                    if (!authdb.containsKey(key)) {
                        authdb.put(key, new JSONObject());
                    }

                    JSONObject profile = (JSONObject) authdb.get(key);

                    System.out.println("access2: " + accessToken);
                    profile.put("accessToken", accessToken);

                    if (!profile.containsKey("profiles")) {
                        profile.put("profiles", new JSONObject());
                    }

                    JSONObject profiles = (JSONObject) profile.get("profiles");

                    if (!profiles.containsKey(uuid)) {
                        profiles.put(uuid, new JSONObject());
                    }

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
            getController().getLogger().getLauncherLog().warning("Error updating launcher_profiles.json: " + ex.getMessage());
        }
    }

    public void updateLauncherProfile(String key2) {
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
            getController().getLogger().getLauncherLog().warning("Error updating single profile launcher_profiles.json: " + ex.getMessage());
        }
    }

    public String getLoginResponse() {
        return loginResponse;
    }

    private MainController getController() {
        return controller;
    }

}
