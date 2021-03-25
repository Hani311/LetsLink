package com.example.androidproject;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);


        WebView webView = (WebView) findViewById(R.id.policy);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.privacypolicies.com/live/26f72230-7d3b-418c-967e-15fd51b0f716");



    }
}