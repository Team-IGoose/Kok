package com.example.kok.controller

import com.example.kok.model.WebPage
import com.example.kok.view.WebViewActivity


class WebViewController(private val activity: WebViewActivity) {
    private val webPage: WebPage = WebPage("https://www.naver.com")
    fun loadWebPage() {
        activity.loadUrl(webPage.url)
    }
}