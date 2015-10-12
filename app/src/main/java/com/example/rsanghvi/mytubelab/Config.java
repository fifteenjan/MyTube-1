package com.example.rsanghvi.mytubelab;

/**
 * Created by rsanghvi on 10/11/15.
 */
public class Config {

    private Config() {
    }

    public static final String YOUTUBE_API_KEY = "AIzaSyBHbKZes38iuFdlGEu9uvB9YR5MNvx6T7I";

    public static String getYoutubeApiKey() {
        return YOUTUBE_API_KEY;
    }
}
