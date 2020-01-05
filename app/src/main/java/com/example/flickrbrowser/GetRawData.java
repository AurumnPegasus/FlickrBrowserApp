package com.example.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus
{
    IDLE,
    FAILED_OR_EMPTY,
    PROCESSING,
    NOT_INITIALISED,
    OK
}

class GetRawData extends AsyncTask <String,Void,String>{
    private static final String TAG = "GetRawData";
    private DownloadStatus memberDownloadStatus;
    private final OnDownloadData memberCallBack;

    interface OnDownloadData{
        void onDownloadCompleted(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadData callBack) {
        this.memberDownloadStatus = DownloadStatus.IDLE;
        this.memberCallBack = callBack;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter is " + s);
//        super.onPostExecute(s);
        if(memberCallBack != null)
        {
            memberCallBack.onDownloadCompleted(s,memberDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if(strings == null)
        {
            memberDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try{
            memberDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code is" + response);
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while (true)
            {
                line = reader.readLine();
                if(line == null)
                    break;
                line = line + "\n";
                result.append(line);
            }

            memberDownloadStatus = DownloadStatus.OK;
            return result.toString();
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG, "doInBackground: Invalid URL" + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, "doInBackground: IOException logging data" + e.getMessage());
        }
        catch (SecurityException e)
        {
            Log.e(TAG, "doInBackground: Security exception" + e.getMessage());
        }
        finally {
            if(connection != null)
                connection.disconnect();
        }

        if(reader != null)
        {
            try {
                reader.close();
            }
            catch (IOException e)
            {
                Log.d(TAG, "doInBackground: Error closing Buffered Stream" +  e.getMessage());
            }
        }
        memberDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
