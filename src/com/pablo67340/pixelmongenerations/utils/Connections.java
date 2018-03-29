/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablo67340.pixelmongenerations.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Bryce
 */
public final class Connections {

    public static String HTTPResponse(String link) {
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            return IOUtils.toString(in, encoding);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static String HTTPValidateResponse(URL url, String content) throws Exception {
        byte[] contentBytes = content.getBytes("UTF-8");
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));
        try (OutputStream requestStream = connection.getOutputStream()) {
            requestStream.write(contentBytes, 0, contentBytes.length);
        }
        BufferedReader responseStream;
        if (((HttpURLConnection) connection).getResponseCode() == 204) {
            responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            return "true";
        } else {
            responseStream = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
        }
        StringBuilder everything = new StringBuilder();
        String line;
        while ((line = responseStream.readLine()) != null) {
            everything.append(line);
        }

        responseStream.close();

        return everything.toString();
    }

    public static String HTTPPostResponse(URL url, String content) {
        try {
            byte[] contentBytes = content.getBytes("UTF-8");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));
            try (OutputStream requestStream = connection.getOutputStream()) {
                requestStream.write(contentBytes, 0, contentBytes.length);
            }
            BufferedReader responseStream;
            if (((HttpURLConnection) connection).getResponseCode() == 200) {
                responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } else {
                responseStream = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
            }
            StringBuilder everything = new StringBuilder();
            String line = "";
            while ((line = responseStream.readLine()) != null) {
                everything.append(line);
            }

            responseStream.close();

            return everything.toString();
        } catch (IOException e) {
            return "";
        }

    }

}
