package com.hk.library.common

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import com.hk.library.R
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Created by Administrator on 2018/3/22.
 */
class WebViewActivity : Activity() {
    lateinit var url: String
    lateinit var title: String
    lateinit var agentWeb: AgentWeb
    companion object {
        const val EXTRA_URL = "extraUrl"
        const val EXTRA_TITLE = "extraTitle"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        url = intent.getStringExtra(EXTRA_URL)?:""
        title = intent.getStringExtra(EXTRA_TITLE) ?: "网页标题"
        setView()
    }

    private fun setView() {
        web_title.text = title
        toolbar.setNavigationOnClickListener { finish() }
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView_container,FrameLayout.LayoutParams(FrameLayout
                        .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(resources.getColor(R.color.colorAccent), 1)
                .setWebChromeClient(object : WebChromeClient(){
                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        web_title.text = title
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }


}