package com.example.rsanghvi.mytubelab;

/**
 * Created by kasatswati on 10/14/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TabFragment1 extends Fragment {
    private List<VideoItem> searchResults;
    private Handler handler;
    private ListView videosFound;
    String keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //super.onCreateView(savedInstanceState);
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        videosFound=(ListView)view.findViewById(R.id.videos_found_fragment);

        //videosFound = (ListView)getView().findViewById(R.id.videos_found_fragment);





        handler = new Handler() {
        };

        keyword = getActivity().getIntent().getExtras().getString("keyword");

        if(keyword == null)
            searchOnYoutube("Trending Now");
        else
            searchOnYoutube(keyword);

        addClickListener();

        return view;
    }

    private void addClickListener() {
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                startActivity(intent);
            }

        });
    }


    private void searchOnYoutube(final String keywords){

        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(TabFragment1.super.getActivity());

                searchResults = yc.search(keywords);

                //  searchResults = yc.playlist(keywords);

                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound(){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getActivity().getApplicationContext(), R.layout.video_item, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                TextView description = (TextView)convertView.findViewById(R.id.video_description);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }


}