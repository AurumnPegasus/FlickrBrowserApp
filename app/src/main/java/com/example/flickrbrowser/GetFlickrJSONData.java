package com.example.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

class GetFlickrJSONData extends AsyncTask<String, Void, List<Photo>> implements  GetRawData.OnDownloadData {
    private List<Photo> memberPhotoList = null;
    private String baseUrl;
    private String memberLanguage;
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
        if(memberCallBack != null)
        {
            memberCallBack.onDataAvailable(memberPhotoList, DownloadStatus.OK);
        }
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        String destinationUri = createUri(params[0], memberLanguage, memberMatchAll);//Creates the whole url with all the appropriate api calls
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInsideThread(destinationUri);//Non Async Call to GetRawData
        return memberPhotoList;
    }


    //Isnt used anywhere, is there primarly as a blueprint so it can be used in other programs
    void executeOnSameThread(String searchCriteria){
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, memberLanguage, memberMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
    }

    private String createUri(String searchCriteria,String lang, boolean matchAll){
        return Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode", matchAll? "ALL":"ANY")
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void onDownloadCompleted(String data, DownloadStatus status)//call back from GetRawData
    {
        if(status == DownloadStatus.OK)
        {
            memberPhotoList = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsArray = jsonObject.getJSONArray("items");//appropriate tags
                for(int i=0;i<itemsArray.length();i++)
                {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);//get first object which is enclosed withing <item> tags
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String tags = jsonPhoto.getString("tags");
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoURL = jsonMedia.getString("m");
                    String link = photoURL.replaceFirst("_m.","_b.");//changing the size of the image from medium to big

                    Photo photoObject = new Photo(title, author, photoURL, link, tags);
                    memberPhotoList.add(photoObject);
                }
            }
            catch (JSONException e)
            {
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if(memberCallBack != null && runningOnSameThread)
        {
            //doesnt come here, it is mainly for if I want to use executeOnSameThread
            memberCallBack.onDataAvailable(memberPhotoList, status);//call back to MainActivity
        }
    }
}