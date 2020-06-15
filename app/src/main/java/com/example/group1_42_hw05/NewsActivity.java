package com.example.group1_42_hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
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

public class NewsActivity extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    static String URL_KEY = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listView = findViewById(R.id.newslistview);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        if(getIntent() != null && getIntent().getExtras() != null) {
            Source source = (Source) getIntent().getSerializableExtra(MainActivity.SOURCE_KEY);
            setTitle(source.name);
            if (isConnected()) {
                new LoadNews().execute("https://newsapi.org/v2/top-headlines?" + "sources=" + source.id + "&apiKey=af6089c3fd1346398a4faf21c53c9dcc");
            } else {
                Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class LoadNews extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList<News> newsList = new ArrayList();
            try {
                URL url = new URL(strings[0]);
                Log.d("Demo", strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
               // Log.d("Demo", json);
                JSONObject root = new JSONObject(json);
                JSONArray jsonArray = root.getJSONArray("articles");
                for(int i = 0; i<jsonArray.length(); i++) {
                    JSONObject article = jsonArray.getJSONObject(i);
                    News news = new News();
                    news.author = article.getString("author");
                    news.title = article.getString("title");
                    news.url = article.getString("url");
                    news.urlToImage = article.getString("urlToImage");
                    news.publishedAt = article.getString("publishedAt");
                    news.description = article.getString("description");
                    newsList.add(news);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newsList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final ArrayList arrayList) {
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(arrayList);
            if(arrayList.size()>0) {
               // Log.d("Demo", arrayList.get(0).toString());
                NewsAdapter adapter = new NewsAdapter(NewsActivity.this, R.layout.news_custom, arrayList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        News news = (News) arrayList.get(i);
                        Intent intent = new Intent(NewsActivity.this, WebViewActivity.class);
                        intent.putExtra(URL_KEY, news.url);
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
