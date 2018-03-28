package com.adventure;

import com.adventure.Layout;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonParser {

    private static final int STATUS_OK = 200;

    /**
     * Generates com.adventure.Layout object from JSON from a given url.
     * Derived from code created by zilles on 9/19/17.
     * @param url - URL which contains JSON
     * @return - com.adventure.Layout object generated from JSON
     */
    public static Layout getJsonObjectFromUrl(String url) {

        HttpResponse<String> stringHttpResponse = null;
        Layout layoutFromJson = null;

        // Make an HTTP request to the above URL
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return layoutFromJson;
        }

        try {
            stringHttpResponse = Unirest.get(url).asString();
        } catch (UnirestException e){
            System.out.println("Network not responding");
            return layoutFromJson;
        }

        if (stringHttpResponse.getStatus() == STATUS_OK) {
            String json = stringHttpResponse.getBody();
            Gson gson = new Gson();
            layoutFromJson = gson.fromJson(json, Layout.class);
        }

        return layoutFromJson;
    }

    /**
     * Generates Layout object from JSON from a given file path.
     * Code derived by zilles, published on 9/8/17.
     *
     * @param fileName that is included in the data folder
     * @return a String containing the contents of the JSON object
     */
    public static Layout getJsonObjectFromFile(String fileName) {

        final Path path = FileSystems.getDefault().getPath("data", fileName);
        String fileContents;

        try {
            fileContents =  new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.out.println("Couldn't find file: " + fileName);
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(fileContents, Layout.class);
    }

}
