package com.example.rsanghvi.mytubelab;

/**
 * Created by kasatswati on 10/14/15.
 */

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView playerView;
    private Button fav;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_player);

        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize(Config.getKEY(), this);

       /*  fav = (Button)findViewById(R.id.favourite);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToFavourite();
                }
            });
*/
        TextView title = (TextView) findViewById(R.id.video_title_playeractivity);
        TextView views = (TextView) findViewById(R.id.video_views_playeractivity1);
        TextView publishedDate = (TextView) findViewById(R.id.video_published_date_playeractivity);
        TextView description = (TextView) findViewById(R.id.description_playeractivity);

        title.setText(getIntent().getStringExtra("title"));
        views.setText(getIntent().getStringExtra("views"));
        publishedDate.setText(getIntent().getStringExtra("date"));
        description.setText(getIntent().getStringExtra("description"));


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean restored) {
        if(!restored){
            player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
        }
    }

//    public void addToFavourite(){
//        String videoId = getIntent().getStringExtra("VIDEO_ID");
//        try {
//            PlaylistUpdates.insertPlaylistItem("PLe2czQmn6x18TWcKx0gyRyHbyKfkdApYM", videoId);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }



}