package com.example.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJSONData extends AsyncTask<String, Void, List<Photo>> implements  GetRawData.OnDownloadData {

    private static final String TAG = "GetFlickrJSONData";
    private List<Photo> memberPhotoList = null;
    private String baseUrl = null;
    private String memberLanguage = null;
    private boolean memberMatchAll;
    private final OnDataAvailable memberCallBack;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJSONData(String baseUrl, String memberLanguage, boolean memberMatchAll, OnDataAvailable memberCallBack) {

        this.baseUrl = baseUrl;
        this.memberLanguage = memberLanguage;
        this.memberMatchAll = memberMatchAll;
        this.memberCallBack = memberCallBack;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if(memberCallBack != null)
        {
            //Log.d(TAG, "onPostExecute: I am calling back");
            memberCallBack.onDataAvailable(memberPhotoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: Ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0], memberLanguage, memberMatchAll);
        Log.d(TAG, "doInBackground: destination uri is " + destinationUri);
        //runningOnSameThread = true;
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInsideThread(destinationUri);
        Log.d(TAG, "doInBackground: Ends");
        return memberPhotoList;
    }


    //Isnt used anywhere, is there primarly as a blueprint so it can be used in other programs
    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, memberLanguage, memberMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: Ends");
    }

    private String createUri(String searchCriteria,String lang, boolean matchAll){
        Log.d(TAG, "createUri: Starts");

        return Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode", matchAll? "ALL":"ANY")
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void onDownloadCompleted(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadCompleted: start status " + status);
        if(status == DownloadStatus.OK)
        {
            memberPhotoList = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                Log.d(TAG, "onDownloadCompleted: number of iterations is " + itemsArray.length());
                for(int i=0;i<itemsArray.length();i++)
                {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorid = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoURL = jsonMedia.getString("m");
                    String link = photoURL.replaceFirst("_m.","_b.");

                    Photo photoObject = new Photo(title, author, authorid, photoURL, link, tags);
                    memberPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadCompleted " + photoObject.toString());
                }
            }
            catch (JSONException e)
            {
                Log.e(TAG, "onDownloadCompleted: error processing json data "+ e.getMessage() );
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        //if(memberCallBack!=null)
            //Log.d(TAG, "onDownloadCompleted: Member cal back isnt null");

        Log.d(TAG, "onDownloadCompleted: running on same thread is " + runningOnSameThread);
        if(memberCallBack != null && runningOnSameThread)
        {
            Log.d(TAG, "onDownloadCompleted: I am here");
            memberCallBack.onDataAvailable(memberPhotoList, status);
        }
        Log.d(TAG, "onDownloadCompleted: Ends");
    }
}