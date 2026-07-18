package com.replayx.app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.replayx.app.R

class SensiActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    inner class AndroidBridge {
        @JavascriptInterface
        fun finish() {
            this@SensiActivity.finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensi)

        webView = findViewById(R.id.webViewSensi)
        findViewById<android.widget.Button>(R.id.btnSensiVoltar).setOnClickListener { finish() }

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            allowFileAccess = true
            allowContentAccess = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(false)
            displayZoomControls = false
        }

        webView.addJavascriptInterface(AndroidBridge(), "Android")
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/sensi.html")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
