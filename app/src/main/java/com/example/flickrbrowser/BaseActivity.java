package com.example.flickrbrowser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";//Key for getting the data from SharedPreferenceManager
    //also is declared in Base Activity so that multiple classes can access the information
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";//Same as above, passes photo
    
    void activateToolBar(boolean enableHome)
    {
        //Action Bar
        //https://developer.android.com/reference/android/support/v7/app/ActionBar

        ActionBar actionBar = getSupportActionBar();
        //Retrieve a reference to this activity's ActionBar.
        //https://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html#getSupportActionBar()

        if(actionBar == null)
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            if(toolbar!=null)
            {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(enableHome);//The back button
        }
    }
}
