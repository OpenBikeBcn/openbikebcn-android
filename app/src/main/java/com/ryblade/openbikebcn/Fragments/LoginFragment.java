package com.ryblade.openbikebcn.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryblade.openbikebcn.LoginActivity;
import com.ryblade.openbikebcn.Model.User;
import com.ryblade.openbikebcn.OnPageLoaded;
import com.ryblade.openbikebcn.R;
import com.ryblade.openbikebcn.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexmorral on 20/12/15.
 */
public class LoginFragment extends Fragment {

    private WebView webView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        webView = (WebView) rootView.findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new MyJavaScriptInterface((LoginActivity) getActivity()), "HTMLOUT");

        webView.loadUrl("http://openbike.byte.cat/loginmobile");

        MyWebViewClient myWebViewClient = new MyWebViewClient();

        webView.setWebViewClient(myWebViewClient);

        return rootView;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://openbike.byte.cat/loginmobile")) {
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
