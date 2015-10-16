package com.example.rsanghvi.mytubelab;

/**
 * Created by kasatswati on 10/14/15.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by kasatswati on 10/12/15.
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (!(isNetworkAvailable(this.getApplicationContext()))) {
            Toast.makeText(this, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
        }


        searchInput = (EditText)findViewById(R.id.search_input);


        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Intent intent = new Intent(getApplicationContext(), MainAppPage.class);
                    intent.putExtra("keyword", v.getText().toString());
                    startActivity(intent);


                    return false;
                }
                return true;
            }
        });

    }


    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

