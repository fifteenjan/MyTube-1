package com.example.rsanghvi.mytubelab;

/**
 * Created by kasatswati on 10/14/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TabFragment2 extends Fragment {

    private List<VideoItem> searchResults;
    private Handler handler;
    private ListView videosFound;
    private String keyword;
    private PlayListOperations playListOperations;
    private Playlist playlist;
   // private CheckBox checkbox;
    public static final String TAG = "TabFragment2";
    private static List<String> selectedVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        selectedVideos = new ArrayList<String>();
        videosFound = (ListView) view.findViewById(R.id.videos_found_fragment_2);
        handler = new Handler();
        addClickListener();
        searchOnYoutube();
        return view;
    }

    private void addClickListener() {

        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                intent.putExtra("title", searchResults.get(pos).getTitle());
                intent.putExtra("description", searchResults.get(pos).getDescription());
                intent.putExtra("views", searchResults.get(pos).getViewCount().toString());
                intent.putExtra("date", searchResults.get(pos).getPublishedAt());
                startActivity(intent);
            }
        });
    }

    public void searchOnYoutube(){

        new Thread(){
            public void run(){
                searchResults = getFavouriteVideos();

                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound(){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getActivity().getApplicationContext(),
                R.layout.video_item, searchResults){

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.video_item_fav,
                            parent, false);
                }

                /* Declaring references from the Convert View*/
                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_title);
                TextView views = (TextView) convertView.findViewById(R.id.video_views);
                TextView publishedDate = (TextView) convertView.findViewById(R.id.video_published_date);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

                /* Making the object from searchlist from where we will set value in next step.*/
                final VideoItem searchResult = searchResults.get(position);

                /* Setting the value from searchresult to convertView object.*/
                Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                // views.setText(searchResult.getViewCount().toString());
                publishedDate.setText(searchResult.getPublishedAt());

                /* Setting the on click listener for checked item for deletion*/
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String selectedVideoId = searchResult.getId();
                        if (((CheckBox) v).isChecked()) {
                            selectedVideos.add(selectedVideoId);
                            Log.i(TAG, "Video got selected");
                        }
                        else {
                            if (selectedVideos.contains(selectedVideoId)) {
                                selectedVideos.remove(selectedVideoId);
                                Log.d("Removed video", selectedVideoId);
                            } else
                                Log.d("Nothing done with video", selectedVideoId);
                        }
                    }
                });
                return convertView;
            }
        };
        videosFound.setAdapter(adapter);
    }

    public List<VideoItem> getFavouriteVideos() {
        /* Get User playlist*/
        playListOperations = new PlayListOperations();
        PlaylistListResponse playlistListResponse = playListOperations.getUserPlaylists();

        playlist = playListOperations.getPlayList(Constants.TITLE, playlistListResponse);
        /* If no playlist then create one new*/
        if (playlist == null) {
            Log.i(TAG, "Playlist is empty and need to be created");
            playlist = playListOperations.createPlayList(Constants.TITLE, Constants.DESCRIPTION);
            if (playlist == null) {
                Log.e(TAG, "Error in creating new playlist with title " + Constants.TITLE);
            }
            return null;
        }
        Log.i(TAG, playlist.toString());

        PlaylistItemListResponse playlistItemListResponse = playListOperations.listItemInPlayList(playlist);
        Log.i(TAG, playlistItemListResponse.toString());
        List<PlaylistItem> playlistItems = playlistItemListResponse.getItems();
        List<VideoItem> videoItemList = new ArrayList<VideoItem>();

        for (PlaylistItem playlistItem : playlistItems) {
            VideoItem item = new VideoItem();
            item.setTitle(playlistItem.getSnippet().getTitle());
            item.setDescription(playlistItem.getSnippet().getDescription());
            item.setThumbnailURL(playlistItem.getSnippet().getThumbnails().getDefault().getUrl());
            item.setId(playlistItem.getSnippet().getResourceId().getVideoId());
            item.setPublishedAt(String.valueOf(playlistItem.getSnippet().getPublishedAt()).substring(0, 9));
            videoItemList.add(item);
        }
        Log.i(TAG,videoItemList.toString());
        return videoItemList;
    }

    public static void deleteVideoFromFav() {
        PlayListOperations playListOperations = new PlayListOperations();
        PlaylistListResponse playlistListResponse = playListOperations.getUserPlaylists();
        Playlist playlist = playListOperations.getPlayList(Constants.TITLE, playlistListResponse);
        Log.i("delete",playlist.toString());
        PlaylistItemListResponse playlistItemListResponse = playListOperations.listItemInPlayList(playlist);
        Log.i("delete + playlistitem", playlistItemListResponse.getItems().toString());
        List<PlaylistItem> playlistItems = playlistItemListResponse.getItems();
        for(PlaylistItem playlistItem : playlistItems) {
            PlaylistItemSnippet snipper = playlistItem.getSnippet();
            Log.i("delete + snippet",snipper.getResourceId().getVideoId());
            Log.i("Delete + selectedVideo",selectedVideos.toString());
            if(selectedVideos.contains(snipper.getResourceId().getVideoId())) {
                playListOperations.deleteItemFromPlayList(playlistItem.getId());
            }
        }
    }
}


