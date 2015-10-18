package com.example.rsanghvi.mytubelab;

/**
 * Created by Rishabh Sanghvi on 10/14/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TabFragment1 extends Fragment {
    private List<VideoItem> searchResults;
    private Handler handler;
    private ListView videosFound;
    String keyword;
    boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //super.onCreateView(savedInstanceState);
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        videosFound = (ListView) view.findViewById(R.id.videos_found_fragment);
        handler = new Handler();
        keyword = getActivity().getIntent().getExtras().getString("keyword");
        if (keyword == null)
            searchOnYoutube("Trending Now");
        else
            searchOnYoutube(keyword);

        return view;
    }

    private void searchOnYoutube(final String keywords) {

        new Thread() {
            public void run() {
                PlayListOperations playListOperations = new PlayListOperations();
                searchResults = playListOperations.searchByKeyword(keywords);
                handler.post(new Runnable() {
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound() {
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getActivity().getApplicationContext(), R.layout.video_item, searchResults) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }
                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_title);
                TextView views = (TextView) convertView.findViewById(R.id.video_views);
                TextView publishedDate = (TextView) convertView.findViewById(R.id.video_published_date);
                final ImageButton favbutton = (ImageButton) convertView.findViewById(R.id.favouriteCheckBoxOn);
                //    TextView description = (TextView)convertView.findViewById(R.id.description);


                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                views.setText(searchResult.getViewCount().toString());
                publishedDate.setText(searchResult.getPublishedAt());
                //  description.setText(searchResult.getDescription());

                final int pos = position;

                thumbnail.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), PlayerActivity.class);
                        intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                        intent.putExtra("title", searchResults.get(pos).getTitle());
                        // intent.putExtra("description", searchResults.get(pos).getDescription());
                        intent.putExtra("views", searchResults.get(pos).getViewCount().toString());
                        intent.putExtra("date", searchResults.get(pos).getPublishedAt());
                        startActivity(intent);
                    }
                });

                favbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (searchResults.get(pos).isFavFlag() == false) {
                            PlayListOperations playListOperations = new PlayListOperations();
                            PlaylistListResponse playlistListResponse = playListOperations.getUserPlaylists();
                            Playlist playlist = playListOperations.getPlayList(Constants.TITLE, playlistListResponse);
                            playListOperations.insertItemInPlaylist(playlist, "My new Item", searchResults.get(pos).getId().toString());
                            favbutton.setBackgroundResource(R.drawable.favfilled);
                            flag = true;
                        } else {
                            favbutton.setBackgroundResource(R.drawable.bookmark);
                            searchResults.get(pos).setFavFlag(true);
                        }
                    }
                });
                return convertView;
            }
        };
        videosFound.setAdapter(adapter);
    }
}