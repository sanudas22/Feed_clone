package com.example.group1_42_hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setTitle("Web Activity");

        if (getIntent() != null && getIntent().getExtras() != null) {
            if(isConnected()) {
                String url = getIntent().getStringExtra(NewsActivity.URL_KEY);
                WebView myWebView = new WebView(WebViewActivity.this);
                myWebView.setWebViewClient(new WebViewClient());
                myWebView.loadUrl(url);
                setContentView(myWebView);
            } else {
                Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
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
