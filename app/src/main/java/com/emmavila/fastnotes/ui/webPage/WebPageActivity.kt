package com.emmavila.fastnotes.ui.webPage

import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emmavila.fastnotes.databinding.ActivityWebPageBinding

class WebPageActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityWebPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWebPageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initWebView()
    }

    private fun initWebView() {
        with(mBinding.wvPage) {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    Toast.makeText(this@WebPageActivity, message, Toast.LENGTH_LONG).show()
                    result?.confirm()
                    return super.onJsAlert(view, url, message, result)
                }
            }
            loadUrl("https://fast-notes-app.vercel.app/")
        }
    }
}