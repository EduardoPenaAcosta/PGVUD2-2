package com.example.githubhunter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubhunter.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText searchBox;
    TextView urlDisplay;
    TextView searchResults;
    TextView errorDisplay;
    ProgressBar requestProgress;

    public class GitHubQueryTask extends AsyncTask<URL,Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            requestProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {

            URL searchURL = urls[0];
            String gitHubSearchResults = null;

            try {
                gitHubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return gitHubSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            requestProgress.setVisibility(View.INVISIBLE);
            if( s != null && !s.equals("")){
                showJsonData();
                searchResults.setText(s);
            }else{
                showErrorMessage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.search) {
            Log.i("MainActivity", "Se ha completado la búsqueda.");
            Context context = MainActivity.this;
            Toast.makeText(context, R.string.search_pressed, Toast.LENGTH_LONG).show();

            URL githubUrl = NetworkUtils.buildURL(searchBox.getText().toString());


            try {
                JSONObject jsonObject = new JSONObject(githubUrl.toString());

                urlDisplay.setText(jsonObject.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }



            new GitHubQueryTask().execute(githubUrl);

            
        }
        return true;
    }

    private void showJsonData(){
        errorDisplay.setVisibility(View.INVISIBLE);
        searchResults.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        searchResults.setVisibility(View.INVISIBLE);
        errorDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = (EditText) findViewById(R.id.search_box);
        urlDisplay = (TextView) findViewById(R.id.url_display);
        searchResults = (TextView) findViewById(R.id.github_search_result);
        errorDisplay = (TextView) findViewById(R.id.error_message_display);

        requestProgress = (ProgressBar) findViewById(R.id.request_progress);
    }
}