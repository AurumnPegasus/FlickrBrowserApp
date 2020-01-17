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
        if(memberCallBack != null)
        {
            memberCallBack.onDownloadCompleted(s,memberDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if(strings == null)
        {
            memberDownloadStatus = DownloadStatus.NOT_INITIALISED;//if parameter is null that means there is some error
            return null;
        }
        
        try{
            memberDownloadStatus = DownloadStatus.PROCESSING;//in case an error occurs while downloading and it goes to catch block
            URL url = new URL(strings[0]);

            //HttpURLConnection class is an abstract class directly extending from URLConnection class.
            // It includes all the functionality of its parent class with additional HTTP specific features.
            //https://www.geeksforgeeks.org/java-net-httpurlconnection-class-java/

            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //If a string is going to remain constant throughout the program,
            // then use String class object because a String object is immutable.

            //If a string can change (example: lots of logic and operations in the construction of the string)
            // and will only be accessed from a single thread, using a StringBuilder is good enough.

            //If a string can change, and will be accessed from multiple threads,
            // use a StringBuffer because StringBuffer is synchronous so you have thread-safety.
            //https://www.geeksforgeeks.org/string-vs-stringbuilder-vs-stringbuffer-in-java/

            StringBuilder result = new StringBuilder();//Here we are accessing result from only 1 thread
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
                reader.close();//closing the BufferedStream
            }
            catch (IOException e)
            {
                Log.d(TAG, "doInBackground: Error closing Buffered Stream" +  e.getMessage());
            }
        }
        memberDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;//in case the data did not download properly
        return null;
    }

     void runInsideThread(String s)
     {
         if(memberCallBack != null)
         {
             memberCallBack.onDownloadCompleted(doInBackground(s), DownloadStatus.OK);//Non Async call to doInBackground
         }
         Log.d(TAG, "runInsideThread: Ends");
     }


}