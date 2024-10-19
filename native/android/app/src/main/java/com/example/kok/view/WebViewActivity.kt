package com.example.kok.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.kok.R
import com.example.kok.controller.WebViewController

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // WebView 초기화
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient() // 웹뷰 내부에서 페이지 로드
        webView.settings.javaScriptEnabled = true // 자바스크립트 활성화 (필요한 경우만)

        // Controller를 통해 웹 페이지 로드
        val controller = WebViewController(this)
        controller.loadWebPage()
    }

    // Controller에서 호출하는 메소드
    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }
}