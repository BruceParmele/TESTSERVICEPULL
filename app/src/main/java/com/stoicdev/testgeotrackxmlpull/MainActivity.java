package com.stoicdev.testgeotrackxmlpull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {

    private StudentAdapter mAdapter;
    SearchView sv;
    private ListView studentList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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

        checkForCurrentFile();


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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.stoicdev.testgeotrackxmlpull/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.stoicdev.testgeotrackxmlpull/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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

    private void checkForCurrentFile() {


        File rootDataDir = getFilesDir();
        String PATH = rootDataDir.toString();

        File file = new File(PATH + "/StackStudents.xml");
        Log.i("*** FILE PATH *** ", PATH);
        if (file.exists()){
            //Check if file creation date is same as application running date
            Date lastModDate = new Date(file.lastModified());


            Toast.makeText(getApplicationContext(), PATH + " Exists.", Toast.LENGTH_LONG).show(); }

        else {

            Toast.makeText(getApplicationContext(), PATH + " Does NOT Exist.", Toast.LENGTH_LONG).show();

        }  //return false;
    }

}
