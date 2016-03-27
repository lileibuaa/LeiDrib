package lei.buaa.leidrib.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.config.URLConfig;

/**
 * Created by lei on 3/19/16.
 * email: lileibh@gmail.com
 */
public class WebActivity extends BaseActivity {
    @Bind(R.id.wv_web)
    WebView mWebView;
    @Bind(R.id.pb_progress)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String url = getIntent().getStringExtra(KeyConfig.URL);
        if (TextUtils.isEmpty(url))
            url = URLConfig.DEFAULT_URL;
        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.setWebChromeClient(new CustomChromeClient());
        mWebView.loadUrl(url);
    }

    private class CustomChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            Log.d("Web_Info", "on page start " + uri.getHost() + "\t" + uri.getPath());
            if (uri.getHost().equals(URLConfig.DEFAULT_HOST) && uri.getQuery().contains(KeyConfig.CODE)) {
                String code = uri.getQueryParameter(KeyConfig.CODE);
                Intent intent = new Intent();
                intent.putExtra(KeyConfig.CODE, code == null ? "" : code);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
