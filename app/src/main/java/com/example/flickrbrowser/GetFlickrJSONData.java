package com.example.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJSONData implements GetRawData.OnDownloadData {
    private static final String TAG = "GetFlickrJSONData";
    private List<Photo> memberPhotoList = null;
    private String baseUrl = null;
    private String memberLanguage = null;
    private boolean memberMatchAll;
    private final OnDataAvaialble memberCallBack;

    interface OnDataAvaialble {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJSONData(String baseUrl, String memberLanguage, boolean memberMatchAll, OnDataAvaialble memberCallBack) {

        this.baseUrl = baseUrl;
        this.memberLanguage = memberLanguage;
        this.memberMatchAll = memberMatchAll;
        this.memberCallBack = memberCallBack;
    }

    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
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

        if(memberCallBack != null)
        {
            memberCallBack.onDataAvailable(memberPhotoList, status);
        }
        Log.d(TAG, "onDownloadCompleted: Ends");
    }
}



























