package com.example.rsanghvi.mytubelab;

import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.PlaylistLocalization;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsanghvi on 10/16/15.
 */
public class PlayListOperations {

    private YouTube.PlaylistItems playlistitems;
    private YouTube.Playlists playlists;
    private PlaylistListResponse playlistListResponse;
    private static final String TAG = "BackendAPI";

    public PlaylistListResponse getUserPlaylists() {

        playlists = Config.getYoutube().playlists();

        try {
            playlistListResponse = playlists.list("id, snippet")
                    .setOauthToken(Config.getTokenId())
                    .setMine(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (playlistListResponse != null) {
            Log.i("PlaylistResponseItem", "Playlist response is created");
            Log.i("PlaylistResponseItem", playlistListResponse.toString());
        }
        return playlistListResponse;
    }

    public Playlist getPlayList(String name, PlaylistListResponse response) {
        List<Playlist> playlists = response.getItems();
        if (playlists.isEmpty()) {
            return null;
        }
        for (Playlist playlist : playlists) {
            PlaylistSnippet playlistSnippet = playlist.getSnippet();
            PlaylistLocalization playlistLocalization = playlistSnippet.getLocalized();
            Log.i("PlaylistTitle", playlistLocalization.getTitle());
            if (playlistLocalization.getTitle().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        return null;
    }

    public Playlist createPlayList(String title, String description) {
        PlaylistSnippet playlistSnippet = new PlaylistSnippet();
        playlistSnippet.setTitle(title);
        playlistSnippet.setDescription(description);

        Playlist playlist = new Playlist();
        playlist.setSnippet(playlistSnippet);

        Playlist newPlayList = null;

        try {
            newPlayList = Config.getYoutube().playlists()
                    .insert("snippet,status", playlist)
                    .setOauthToken(Config.getTokenId()).execute();

        } catch (IOException e) {
            Log.e("Error creating playlist", e.toString());
            e.printStackTrace();
        }

        Log.i("newPlayList", newPlayList.toString());

        return newPlayList;
    }

    public void insertItemInPlaylist(Playlist playlist, String title, String videoId) {

        ResourceId resourceId = new ResourceId();
        resourceId.setKind(Constants.RESOURCE_KIND);
        //resourceId.setVideoId("lYNNgGBVtTo");
        resourceId.setVideoId(videoId);


        PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
        playlistItemSnippet.setTitle("First video in the test playlist");

        playlistItemSnippet.setPlaylistId(playlist.getId());

        playlistItemSnippet.setResourceId(resourceId);

        PlaylistItem playlistItem = new PlaylistItem();
        playlistItem.setSnippet(playlistItemSnippet);

        try {
            PlaylistItem returnedPlaylistItem = Config
                    .getYoutube()
                    .playlistItems()
                    .insert("snippet", playlistItem)
                    .setOauthToken(Config.getTokenId())
                    .execute();
            Log.i("returnedPlaylistItem", returnedPlaylistItem.toString());

        } catch (IOException e) {
            Log.e("Error inserting item", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteItemFromPlayList(String playlistItemId) {

        try {
            Config
                    .getYoutube().playlistItems()
                    .delete(playlistItemId)
                    .setOauthToken(Config.getTokenId())
                    .execute();


        } catch (IOException e) {
            Log.e("Error inserting item", e.toString());
            e.printStackTrace();
        }
        Log.i(TAG, "Successfully deleted.");
    }

    public List<VideoItem> searchByKeyword(String keywords) {
        SearchListResponse response = new SearchListResponse();
        try {
            //Search
            response =
                    Config.getYoutube().search().list("id,snippet")
                            .setOauthToken(Config.getTokenId())
                            .setType("video")
                            .setMaxResults((long) 10)
                            .setQ(keywords)
                            .setFields("items(id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url)")
                            .execute();
        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e);
        }
        List<SearchResult> results = response.getItems();
        List<VideoItem> items = new ArrayList<VideoItem>();
        List<Video> videoList = attachId(results);
        int i = 0;

        for (SearchResult result : results) {

            Log.i("Result", result.toString());

            VideoItem item = new VideoItem();
            item.setTitle(result.getSnippet().getTitle());
            item.setDescription(result.getSnippet().getDescription());
            item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
            item.setId(result.getId().getVideoId());
            item.setPublishedAt(String.valueOf(result.getSnippet().getPublishedAt()).substring(0, 9));
            item.setViewCount(videoList.get(i).getStatistics().getViewCount());
            item.setFavFlag(false);
            i = i + 1;
            items.add(item);
            Log.i("published At : ", String.valueOf(result.getSnippet().getPublishedAt()).substring(0, 9));
        }
        return items;
    }

    public List<Video> attachId(List<SearchResult> searchResultList) {

        List<String> videoIds = new ArrayList<String>();

        try {
            if (searchResultList != null) {

                // Merge video IDs
                for (SearchResult searchResult : searchResultList) {
                    videoIds.add(searchResult.getId().getVideoId());
                }
                Joiner stringJoiner = Joiner.on(',');
                String videoId = stringJoiner.join(videoIds);

                Log.i("VideoId", videoId);
                YouTube.Videos.List listVideosRequest = Config.getYoutube().videos().list("statistics").setId(videoId);
                listVideosRequest.setKey(Config.getKEY());
                VideoListResponse listResponse = listVideosRequest.execute();
                List<Video> videoList = listResponse.getItems();

                if (!videoList.isEmpty())
                    return (videoList);

            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public List<String> getListOfVideoIDForPlaylistItemListResponse(PlaylistItemListResponse playlistItemListResponse) {
        List<String> videoIDs = new ArrayList<String>();
        List<PlaylistItem> playlistItems = playlistItemListResponse.getItems();
        for (PlaylistItem playlistItem : playlistItems) {
            String videoId = playlistItem.getContentDetails().getVideoId();
            videoIDs.add(videoId);
        }
        return videoIDs;
    }

    public PlaylistItemListResponse listItemInPlayList(Playlist playlist) {
        playlistitems = Config.getYoutube().playlistItems();
        PlaylistItemListResponse playlistItemsResponse = null;
        try {
            playlistItemsResponse = playlistitems
                    .list("id,snippet,contentDetails")
                    .setOauthToken(Config.getTokenId())
                    .setPlaylistId(playlist.getId())
                            // .setPlaylistId("PLe2czQmn6x18TWcKx0gyRyHbyKfkdApYM")
                    .execute();
        } catch (IOException e) {
            Log.e("Error listing item", e.toString());
            e.printStackTrace();
        }
        Log.i("playlistItems", playlistItemsResponse.toString());

        return playlistItemsResponse;
    }

}
