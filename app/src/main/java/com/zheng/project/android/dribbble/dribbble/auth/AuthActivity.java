package com.zheng.project.android.dribbble.dribbble.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.view.base.BaseActivity;

import butterknife.BindView;

public class AuthActivity extends BaseActivity {

    public static final String KEY_URL = "url";
    public static final String KEY_CODE = "code";

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.webview) WebView webView;

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onViewCreated() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.auth_activity_title));

        progressBar.setMax(100);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(Auth.REDIRECT_URI)) {
                    Uri uri = Uri.parse(url);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KEY_CODE, uri.getQueryParameter(KEY_CODE));
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        String url = getIntent().getStringExtra(KEY_URL);
        webView.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
