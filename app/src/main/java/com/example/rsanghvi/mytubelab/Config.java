package com.example.rsanghvi.mytubelab;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;

/**
 * Created by kasatswati on 10/15/15.
 */
public class Config {

    public static YouTube youtube;

    public static final String KEY
            = "AIzaSyD46Ck3Vj_Qy7rJPb7ZT5mtayWNQXg-Myo";

    public static String tokenId;

    public static void setTokenId(String tokenId) {
        Config.tokenId = tokenId;
    }

    public static String getKEY() {
        return KEY;
    }
    public static String getTokenId() {
        return tokenId;
    }

    public static YouTube getYoutube() {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
            }
        }).setApplicationName("MyTubeLab").build();
        return youtube;
    }





}