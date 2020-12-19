package com.epayeats.epayeatsdeliverypartner.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.epayeats.epayeatsdeliverypartner.R;

public class Directions_Activity extends AppCompatActivity
{
    WebView webView;

    // source
    String lang;
    String lot;
    String getloc_location;

    // destination
    String user_lat;
    String user_long;
    String userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_);

        // destination
        user_lat = getIntent().getExtras().getString("user_lat");
        user_long = getIntent().getExtras().getString("user_long");
        userLocation = getIntent().getExtras().getString("userLocation");

        // source
        lang = getIntent().getExtras().getString("lang");
        lot = getIntent().getExtras().getString("lot");
        getloc_location = getIntent().getExtras().getString("getloc_location");

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.canGoBack();
        webView.clearHistory();
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true,false);
            }
        });

        webView.getSettings().setGeolocationDatabasePath(this.getFilesDir().getPath());

        webView.loadUrl("http://maps.google.com/maps?" + "saddr="+Double.parseDouble(lang)+","+Double.parseDouble(lot) + "&daddr="+Double.parseDouble(user_lat)+","+Double.parseDouble(user_long));

    }
}