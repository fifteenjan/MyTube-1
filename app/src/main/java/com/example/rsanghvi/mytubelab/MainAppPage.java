package com.example.rsanghvi.mytubelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainAppPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Seach"));

        tabLayout.addTab(tabLayout.newTab().setText("Playlist"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 //       searchResults =  getIntent().getParcelableArrayListExtra("searchresult");
//        if(in!=null) {
//            Bundle bundle = getIntent().getExtras();
//            searchResults = (List<VideoItem>) bundle.get("searchResults");
//            updateVideosFound();
//
//        }

//        handler = new Handler() {
//        };
//        searchOnYoutube("Dheere Dheere");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_app_page, menu);

        return super.onCreateOptionsMenu(menu);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
       if (id == R.id.action_search) {
           Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
           startActivity(intent);
           return true;
        }
        return super.onOptionsItemSelected(item);
    }



}