package com.openclassrooms.realestatemanager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnection {

    public static String startHttpRequest(String urlString) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();
            InputStream in = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (MalformedURLException exception) {

        } catch (IOException exception) {

        } catch (Exception e) {

        }

        return stringBuilder.toString();
    }

}
