package com.mabrouk.moviedb.common;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mabrouk.moviedb.R;


public class WebviewActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    WebView webView;
    String title;
    String url;
    ProgressBar pBar;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        refreshLayout.setOnRefreshListener(() -> webView.reload());

        webView = (WebView) findViewById(R.id.webview);
        pBar = (ProgressBar) findViewById(R.id.progressBar);
        pBar.getProgressDrawable()
                .setColorFilter(getResources().getColor(R.color.colorAccent),PorterDuff.Mode.SRC_IN);

        title = getIntent().getStringExtra(EXTRA_TITLE);
        setTitle(title);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100) {
                    pBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }else{
                    pBar.setVisibility(View.VISIBLE);
                    pBar.setProgress(newProgress);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        url = getIntent().getStringExtra(EXTRA_URL);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
