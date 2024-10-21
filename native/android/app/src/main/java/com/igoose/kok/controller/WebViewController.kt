package com.igoose.kok.controller

import com.igoose.kok.model.WebPage
import com.igoose.kok.view.WebViewActivity


class WebViewController(private val activity: WebViewActivity) {
    private val webPage: WebPage = WebPage("https://www.naver.com")
    fun loadWebPage() {
        activity.loadUrl(webPage.url)
    }
}