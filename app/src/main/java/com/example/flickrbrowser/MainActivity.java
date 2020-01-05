package com.example.flickrbrowser;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJSONData.OnDataAvaialble {
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

 //       GetRawData getRawData = new GetRawData(this);
  //      getRawData.execute("https://www.flickr.com/services/feeds/photos_public.gne?tags=fanart&format=json&nojsoncallback=1");
        Log.d(TAG, "onCreate: ended");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: Starts");
        super.onResume();
        GetFlickrJSONData getFlickrJSONData = new GetFlickrJSONData("https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true, this);
        getFlickrJSONData.execute("world war, memes");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        if(status == DownloadStatus.OK){
            Log.d(TAG, "onDataAvailable: success " + data);
        }
        else{
            Log.d(TAG, "onDataAvailable: failure " + status);
        }
    }
}
