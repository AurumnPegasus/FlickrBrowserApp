package com.example.flickrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {
    private SearchView memberSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activateToolBar(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        //Return the handle to a system-level service by class.
        //https://developer.android.com/reference/android/content/Context.html#getSystemService(java.lang.Class%3CT%3E)

        memberSearchView = (SearchView)menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        //https://developer.android.com/reference/android/app/SearchManager.html#getSearchableInfo(android.content.ComponentName)
        //https://developer.android.com/reference/android/app/SearchableInfo.html
        //meta-data for an activity. Only applications that search other applications should need to use this class.
        //This class provides access to the system search services.

        memberSearchView.setSearchableInfo(searchableInfo);
        memberSearchView.setIconified(false);
        //Android developers don't need to be messing around with icons and image file types just to get an arrow button to look the way it should look

        memberSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(FLICKR_QUERY,query).apply();
                finish();
                memberSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;//we are not handling it
            }
        });
        memberSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return true;
            }
        });
        return true;
    }
}
