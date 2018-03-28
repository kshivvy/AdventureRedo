package com.adventure;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class JsonParserTest {


    private static Layout testWorld;
    private static String defaultURL = "https://courses.engr.illinois.edu/cs126/adventure/siebel.json";
    private static Adventure testGame;

    @Before
    public void setup() {
        testGame = new Adventure(defaultURL);
        String fileContents = getFileContentsAsString("Siebel.json");
        Gson gson = new Gson();
        testWorld = gson.fromJson(fileContents, Layout.class);
    }

    //Test for valid URL.
    @Test
    public void testGetJsonObjectValidUrl() {
        assertEquals(testWorld, JsonParser.getJsonObjectFromUrl(defaultURL));
    }

    //Test for invalid URL.
    @Test
    public void testGetJsonObjectBadUrl() {
        assertEquals(null, JsonParser.getJsonObjectFromUrl("Bad and Invalid URL"));
    }

    //Test for network error. To demonstrate, turn internet off on device.
    @Test
    public void testGetJsonObjectNetworkError() {
        assertEquals(null, JsonParser.getJsonObjectFromUrl(defaultURL));
    }


    /**
     * Returns the file contents as a String
     * Code derived from zilles, published on 9/8/17.
     * @param fileName that is included in the data folder
     * @return a String containing the contents of the JSON object
     */
    public String getFileContentsAsString(String fileName) {

        final Path path = FileSystems.getDefault().getPath("data", fileName);

        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.out.println("Couldn't find file: " + fileName);
            return null;
        }
    }
}