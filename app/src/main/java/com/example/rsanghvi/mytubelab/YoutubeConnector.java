package com.example.rsanghvi.mytubelab;

import android.content.Context;
import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;
    private YouTube.PlaylistItems playlistitems;
    private YouTube.Playlists playlists;


    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(context.getString(R.string.app_name)).build();


        try{



            String resource = "{ snippet: { description: message}, contentDetails: { bulletin: { resourceId: { kind: 'youtube#video', videoId: videoId}}}}";



            //Search
            query = youtube.search().list("id,snippet");
            query.setKey(Config.getKEY());
            query.setOauthToken(Config.getTokenId());
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url)");


        }catch(IOException e){
            Log.d("YC", "Could not initialize: " + e);
        }
    }

    public List<VideoItem> search(String keywords){
        query.setQ(keywords);
        try{
            //Get User Playlists



            //Get PlayListItem
            playlistitems = youtube.playlistItems();
            PlaylistItemListResponse playlistItemsResponse = playlistitems
                    .list("id,snippet").setKey(Config.getKEY())
                    .setOauthToken(Config.getTokenId())
                    .setPlaylistId("PLe2czQmn6x18TWcKx0gyRyHbyKfkdApYM")
                    .execute();

            Log.i("playlistItems", playlistItemsResponse.toString());

            /** List PlayList Items **/




            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();


            List<VideoItem> items = new ArrayList<VideoItem>();

            List<Video> videoList = attachId(results);

            int i =0;

            for(SearchResult result:results){

                Log.i("Result", result.toString());

                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                item.setPublishedAt(String.valueOf(result.getSnippet().getPublishedAt()).substring(0,9));
                item.setViewCount(videoList.get(i).getStatistics().getViewCount());
                i=i+1;
                items.add(item);
                Log.i("published At : ", String.valueOf(result.getSnippet().getPublishedAt()).substring(0, 9));
            }
            for(VideoItem item:items){

                    Log.i("item id",item.getId());
                //String date = DateFormat.getDateTimeInstance().format(item.getPublishedAt());
              //  Log.i("item date", date);
             //   Log.i("item",item.);
              //  Log.i("item", String.valueOf(item.getPublishedAt()));


            }

            return items;
        } catch (IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }


    public  List<Video> attachId(List<SearchResult> searchResultList){

       Log.i("I was called: ", "I was Called");

        List<String> videoIds = new ArrayList<String>();

        try{
            if (searchResultList != null) {

                // Merge video IDs
                for (SearchResult searchResult : searchResultList) {
                    videoIds.add(searchResult.getId().getVideoId());
                }
                Joiner stringJoiner = Joiner.on(',');


                String videoId = stringJoiner.join(videoIds);

                Log.i("VideoId",videoId);
                // Call the YouTube Data API's youtube.videos.list method to
                // retrieve the resources that represent the specified videos.
                YouTube.Videos.List listVideosRequest = youtube.videos().list("statistics").setId(videoId);
                listVideosRequest.setKey(Config.getKEY());
                VideoListResponse listResponse = listVideosRequest.execute();


                List<Video> videoList = listResponse.getItems();

                if(!videoList.isEmpty())
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


//    URL url = new URL("http://www.android.com/");
//    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//    try {
//        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//        readStream(in);
//        finally {
//            urlConnection.disconnect();
//        }
//    }

}


