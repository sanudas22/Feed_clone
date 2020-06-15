package com.example.group1_42_hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * a. Homework 05
 * b. File Name.: Group1_42_HW05
 * c. Full name of students of Group1 42.: SANU DAS
 */
public class MainActivity extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    static String SOURCE_KEY = "SOURCE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main Activity");
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        listView = findViewById(R.id.listview);
        if (isConnected()) {
            new LoadSource().execute("https://newsapi.org/v2/sources?apiKey=af6089c3fd1346398a4faf21c53c9dcc");
        } else {
            Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
        }

    }

    public class LoadSource extends AsyncTask<String, Void, ArrayList<Source>> {

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList<Source> sourceList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject rootObject = new JSONObject(json);
                JSONArray jsonArray = rootObject.getJSONArray("sources");
                for(int i =0; i<jsonArray.length(); i++) {
                    JSONObject newsSource = jsonArray.getJSONObject(i);
                    Source source = new Source();
                    source.id = newsSource.getString("id");
                    source.name = newsSource.getString("name");
                    sourceList.add(source);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sourceList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final ArrayList<Source> arrayList) {
            progressBar.setVisibility(View.INVISIBLE);
            final HashMap sourceDetails = new HashMap();
            super.onPostExecute(arrayList);
            if(arrayList.size()>0) {
                ArrayAdapter<Source> adapter
                        = new ArrayAdapter<Source>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Source source = (Source) arrayList.get(i);
                        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(SOURCE_KEY, source);

                        intent.putExtras(bundle);
                        //intent.putExtra(SOURCE_KEY, source);
                        startActivity(intent);

                    }
                });
            }
        }
    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

}
