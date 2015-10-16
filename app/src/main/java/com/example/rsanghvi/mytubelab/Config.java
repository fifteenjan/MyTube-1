package com.example.rsanghvi.mytubelab;

/**
 * Created by kasatswati on 10/15/15.
 */
public class Config {



    public static final String KEY
            = "AIzaSyD46Ck3Vj_Qy7rJPb7ZT5mtayWNQXg-Myo";

    public static void setTokenId(String tokenId) {
        Config.tokenId = tokenId;
    }

    public static String getKEY() {
        return KEY;
    }
    public static String getTokenId() {
        return tokenId;
    }

    public static String tokenId;



}
