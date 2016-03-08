package com.stoicdev.testgeotrackxmlpull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private StudentAdapter mAdapter;
    SearchView sv;
    private ListView studentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("StackStudent", "OnCreate()");
        setContentView(R.layout.activity_main);

        //Get reference to our ListView
        studentList = (ListView) findViewById(R.id.studentList);
        sv = (SearchView) findViewById(R.id.searchView);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {


                mAdapter.getFilter().filter(text);
                return false;
            }
        });

        //Set the click listener to launch the browser when a row is clicked.
        studentList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                String subjectID = mAdapter.getItem(pos).getSubjectID();
                //Intent i = new Intent(Intent.ACTION_VIEW);
                Toast.makeText(getApplicationContext(), "ID is " + subjectID, Toast.LENGTH_LONG).show();
                //i.setData(Uri.parse(url));
                // startActivity(i);

            }

        });

		/*
         * If network is available download the xml from the Internet.
		 * If not then try to use the local file from last time.
		 */
        if (isNetworkAvailable()) {
            Log.i("Stackstudent", "starting download Task");
            studentDownloadTask download = new studentDownloadTask();
            download.execute();
        } else {
            mAdapter = new StudentAdapter(getApplicationContext(), -1, StudentXmlPullParser.getStackStudentFromFile(MainActivity.this));
            studentList.setAdapter(mAdapter);
        }

    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
     * AsyncTask that will download the xml file for us and store it locally.
     * After the download is done we'll parse the local file.
     */
    private class studentDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {

                // Downloader.DownloadFromUrl("http://10.33.224.225/SQLWS.asmx/GetCalloutXML", openFileOutput("StackStudents.xml", Context.MODE_PRIVATE));


                // Downloader.DownloadFromUrl("https://dl.dropboxusercontent.com/u/5724095/XmlParseExample/stacksites.xml", openFileOutput("StackStudents.xml", Context.MODE_PRIVATE));
                Downloader.DownloadFromUrl("http://10.33.224.225/SQLWS.asmx/GetCalloutXML", openFileOutput("StackStudents.xml", Context.MODE_PRIVATE));

                //   Downloader.DownloadFromUrl("http://10.33.224.225/GetCalloutXML.xml", openFileOutput("StackStudents.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //setup our Adapter and set it to the ListView.
            mAdapter = new StudentAdapter(MainActivity.this, -1, StudentXmlPullParser.getStackStudentFromFile(MainActivity.this));
            studentList.setAdapter(mAdapter);
            Log.i("Stackstudent", "adapter size = " + mAdapter.getCount());
        }
    }

}
