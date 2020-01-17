package com.example.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJSONData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter nFlickrRecyclerViewAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//sets the activity_main as current view
        activateToolBar(false);//the back button is disabled for the main page

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//A LayoutManager is responsible for measuring and positioning item views within a RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));//for adding gesture recognition

        nFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), this); // setting the adapter
        recyclerView.setAdapter(nFlickrRecyclerViewAdapter);//takes the value from the adapter appropriately
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //gets stored data, this is so that whenever Activity is deleted,
        // the app begins from where it was left
        String query_result = sharedPreferences.getString(FLICKR_QUERY, "");//the data is stored with the key FLICKR_QUERY

        if(query_result.length() != 0)//checking for default value
        {
            //creating an object of the class GetFickrJSONData and initialising its constructor
            GetFlickrJSONData getFlickrJSONData = new GetFlickrJSONData("https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true, this);
            getFlickrJSONData.execute(query_result);//calling its execute method ( AsyncTask )
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//inflating the menu layout ( Used for the search option which is available )
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) // if a menu item is selected
    {
        int id = item.getItemId();//get id of the item selected, ideally useful when there are multiple options available
        if(id == R.id.action_search)
        {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);//calling SearchActivity
            return true; //return true to tell the compiler that we have successfully handled this particular case
        }
        return super.onOptionsItemSelected(item);//returns false
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        if(status == DownloadStatus.OK){
            nFlickrRecyclerViewAdapter.loadNewData(data);//calls FlickrRecyclerViewAdapters function to notify the recycle view that we need new data now
        }
        else{
            Log.d(TAG, "onDataAvailable: failure " + status);
        }
    }

    @Override
    public void onItemsClick(View view, int position)//for short click/single click
    {
        Toast.makeText(MainActivity.this, "Short Click at position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position)//for long clicks
    {
        //Whenever you need data from an activity to be in another activity, you can pass data between then while starting the activities.
        // Intents in android offer this convenient way to pass data between activities using Extras
        //https://zocada.com/using-intents-extras-pass-data-activities-android-beginners-guide/

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, nFlickrRecyclerViewAdapter.getPhoto(position));//gets that particular photo and passes it to another activity
        startActivity(intent);
    }
}