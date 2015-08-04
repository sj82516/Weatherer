package com.example.friday;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by zhengyuanjie on 15/6/14.
 */
public class Fragment1 extends Fragment {
    //private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment1,container,false);

        WebView myWebView = (WebView)view.findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyBrowser());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("file:///android_asset/map.html");

        return view;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url == "file:///android_asset/map.html"){
                Log.d(url,"Url:");
                view.loadUrl(url);
            }else{
                Log.d(url,"Url:");
                Toast.makeText(getActivity(),url.replace("file:///android_asset/",""),Toast.LENGTH_SHORT).show();
                MainActivity activity = (MainActivity)getActivity();
                activity.btn2.performClick();
            }
            return true;
        }
    }
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }
        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}