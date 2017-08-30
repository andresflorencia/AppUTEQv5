package com.example.andres_dell.uteqdemo.fragments;


import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andres_dell.uteqdemo.activity.MainActivity;
import com.example.andres_dell.uteqdemo.utils.UIUtil;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.example.andres_dell.uteqdemo.R;


public class WebViewFragment extends android.support.v4.app.Fragment {

    View view;
    Bundle bundle;
    private WebView webview;


    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String url,boolean zoom) {
        WebViewFragment f = new WebViewFragment();
        f.setArguments(arguments(url,zoom));
        return f;
    }

    public static Bundle arguments(String url, boolean zoom) {
        return new Bundler()
                .putString("url", url)
                .putBoolean("zoom",zoom)
                .get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        if (!UIUtil.verificaConexion(view.getContext())) {
            Toast.makeText(view.getContext(), "Comprueba tu conexión a Internet", Toast.LENGTH_LONG).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setProgressStyle(ProgressDialog.BUTTON_POSITIVE);//Aqui podemos cambiar el estilo del mensaje de carga
            progressDialog.setIcon(R.mipmap.ic_launcher);
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
            webview = (WebView) view.findViewById(R.id.wvMenuUniversidaad);
            bundle = getArguments();
            String url = bundle.getString("url");

            //JavaScript
            webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webview.getSettings().setCacheMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setDatabaseEnabled(true);
            webview.getSettings().setAppCacheEnabled(true);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setFocusable(true);
            webview.setFocusableInTouchMode(true);

            if (bundle.getBoolean("zoom"))
                webview.getSettings().setBuiltInZoomControls(true);
            webview.loadUrl(url);
            webview.setWebViewClient(new WebViewClient() {
                // android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
                //android:configChanges="keyboard|keyboardHidden|orientation|screenSize">

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //esto elimina ProgressBar.
                    progressDialog.dismiss();
                }

            });
            webview.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
                        webview.goBack();
                        return true;
                    }
                    return false;
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        if(bundle.getString("titulo")!=null){
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(bundle.getString("titulo"));
        }
        super.onResume();
    }

    public boolean canGoBack() {
        return webview.canGoBack();
    }
}