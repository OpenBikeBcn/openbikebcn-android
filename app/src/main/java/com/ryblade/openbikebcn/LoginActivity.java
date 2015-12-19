package com.ryblade.openbikebcn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryblade.openbikebcn.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements OnPageLoaded {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HTMLOUT");

        webView.loadUrl("http://openbike.byte.cat/login?mobile=true");

        MyWebViewClient myWebViewClient = new MyWebViewClient();

        webView.setWebViewClient(myWebViewClient);

    }
    public void OnPageLoaded() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://openbike.byte.cat/login")) {
                // This is my web site, so do not override; let my WebView load the page


                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            return true;
            return false;
        }

        @Override
        public void onPageFinished (WebView view, String url) {
            webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('pre')[0].innerHTML);");

        }

    }


    class MyJavaScriptInterface
    {
        private OnPageLoaded listener;

        public MyJavaScriptInterface(OnPageLoaded listener) {
            this.listener = listener;
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            Log.v("Process HTML", html);
            try {
                JSONObject userJson = new JSONObject(html);

                int id = userJson.getInt("userId");
                String email = userJson.getString("email");
                String username = userJson.getString("username");

                Utils.getInstance().currentUser = new User(id, email, username);

                listener.OnPageLoaded();


            } catch (JSONException ex) {
                Log.v("JavascriptInterface", ex.toString());
            }

        }
    }
}




