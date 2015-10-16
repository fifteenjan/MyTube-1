package com.example.rsanghvi.mytubelab;

import java.math.BigInteger;

/**
 * Created by kasatswati on 10/14/15.
 */

public class VideoItem {
    private String title;
    private String description;
    private String thumbnailURL;
    private String id;
    private BigInteger viewCount;
    private String publishesAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishesAt;
    }

    public BigInteger getViewCount() { return viewCount;}

    public void setViewCount(BigInteger viewCount){this.viewCount = viewCount;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishedAt(String publishesAt) {
         this.publishesAt=publishesAt;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

}