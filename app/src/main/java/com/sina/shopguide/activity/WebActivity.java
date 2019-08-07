package com.sina.shopguide.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sina.shopguide.R;
import com.sina.shopguide.util.ActivityUtils;
import com.sina.shopguide.util.AppUtils;
import com.sina.shopguide.util.UserPreferences;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public class WebActivity extends BaseActivity {
    public static final String EXTRA_KEY_TITLE = "title";
    public static final String EXTRA_KEY_URL = "url";

    private String url;

    protected WebView webView;

    private WebChromeClient webChromeClient;

    protected TextView tvTitle;

    private String extraTitle;

    protected Map<Integer, String> map = new HashMap<Integer, String>();

    protected int index;

    @TargetApi(19)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(EXTRA_KEY_URL);
        if (org.apache.commons.lang3.StringUtils.isEmpty(url)) {
            url = getIntent().getStringExtra("href");
        }

        extraTitle = getIntent().getStringExtra(EXTRA_KEY_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        tvTitle = ((TextView) findViewById(R.id.title_content));
        if (StringUtils.isEmpty(extraTitle)) {
            tvTitle.setText("正在加载中...");
        } else {
            tvTitle.setText(extraTitle);
        }

        webView = (WebView) findViewById(R.id.web_container);
        webChromeClient = new InnerWebViewClient();
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if(ActivityUtils.handleCommonScheme(WebActivity.this, url)) {
                    return true;
                }
                return false;
            }
        });

        webView.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    default:
                        break;
                }
                return v.onTouchEvent(event);
            }
        });

        // 参数设置
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(false);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString()
                + " guide_android_" + AppUtils.getVersionCode(this));
        loadUrl(webView);
    }

    public void showExtra() {

    }

    protected void loadUrl(WebView webView) {
        setCookie(url);
        webView.loadUrl(url);
        webView.addJavascriptInterface(this, "item");
    }

    ;

    protected void setCookie(String url) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(".guide.com",
                "uid=" + UserPreferences.getUserId() + ";");
        cookieManager.setCookie(".guide.com",
                "token=" + UserPreferences.getUserToken() + ";");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        CookieSyncManager.getInstance().sync();
    }

    public class InnerWebViewClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setWebViewTitile(title);
            index++;
            map.put(index, title);
        }
    }

    public void setWebViewTitile(String title) {
        if (StringUtils.isEmpty(extraTitle)) {
            tvTitle.setText(title);
        }
    }

    public void setPrevWebViewTitile() {
        index--;
        if (StringUtils.isEmpty(extraTitle)) {
            String title = map.get(index);
            if (!TextUtils.isEmpty(title)) {
                setWebViewTitile(title);
            }
        }
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        this.webChromeClient = webChromeClient;
        webView.setWebChromeClient(webChromeClient);
    }
}
