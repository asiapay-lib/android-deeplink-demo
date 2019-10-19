package com.asiapay.app.demo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.asiapay.app.demo.deeplink.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    String testUrl;
    Button btnTest;
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // build the payment url
        testUrl = "https://" + getString(R.string.paydollar_domain) + "/eng/payment/payForm.jsp?secureHash="
                + getString(R.string.paydollar_securehash) + "&amount="
                + getString(R.string.paydollar_amount) + "&currCode="
                + getString(R.string.paydollar_currcode) + "&payType=N&orderRef="
                + getString(R.string.paydollar_orderref) + "&merchantId="
                + getString(R.string.paydollar_mid);

        // initialize ui
        setContentView(R.layout.activity_main);
        btnTest = findViewById(R.id.btn_test);

        btnTest.setOnClickListener(this);
        wv = findViewById(R.id.wv);

        // initialize webview
        wv.loadUrl(testUrl);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "-------URL LOAD------");
                Log.d(TAG, url);

                if (URLUtil.isNetworkUrl(url)) {
                    return false;
                }

                if (url.startsWith("https:") || url.startsWith("http:")) {
                    return true;
                } else {
                    // the following part is to handle the deeplink call

                    // check the url can handle or not
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    PackageManager packageManager = getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                    boolean isIntentSafe = activities.size() > 0;

                    if (isIntentSafe) {
                        // trigger the deep link call
                        startActivity(intent);
                        return true;
                    }

                    return false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        wv.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wv.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // reload the url
            case R.id.btn_test:
                wv.stopLoading();
                wv.loadUrl(testUrl);
                wv.reload();
                break;

        }
    }
}
