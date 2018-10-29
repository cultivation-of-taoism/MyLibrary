package com.hk.library.common


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.hk.library.R
import com.just.agentweb.AbsAgentWebSettings
import com.just.agentweb.AgentWeb
import com.just.agentweb.IAgentWebSettings
import kotlinx.android.synthetic.main.fragment_web_view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class WebViewFragment : LazyLoadFragment() {
    override fun setContentView(): Int {
        return R.layout.fragment_web_view
    }

    lateinit var url: String
    lateinit var agentWeb: AgentWeb
    lateinit var preWeb: AgentWeb.PreAgentWeb
    companion object {
        fun newInstance(url: String): WebViewFragment {
            val webViewFragment = WebViewFragment()
            val bundle = Bundle()
            bundle.putString(WebViewActivity.EXTRA_URL, url)
            webViewFragment.arguments = bundle
            return webViewFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments.getString(WebViewActivity.EXTRA_URL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        LogUtils.v(url)
        preWeb = AgentWeb.with(this)
                .setAgentWebParent(webView_container, FrameLayout.LayoutParams(FrameLayout
                        .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(resources.getColor(R.color.colorAccent), 1)
                .setAgentWebWebSettings(object : AbsAgentWebSettings(){
                    lateinit var agentWeb: AgentWeb
                    override fun bindAgentWebSupport(agentWeb: AgentWeb) {
                        this.agentWeb = agentWeb
                    }

                    override fun getWebSettings(): WebSettings {
                        return super.getWebSettings().apply {
                            //android8.0以上需要关闭安全浏览
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LogUtils.v(safeBrowsingEnabled)
                                safeBrowsingEnabled = false
                            }
                        }
                    }
                })
                .createAgentWeb()
                .ready()
        super.onActivityCreated(savedInstanceState)
    }

    override fun lazyLoad() {
        agentWeb = preWeb.go(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::agentWeb.isInitialized)
            agentWeb.webLifeCycle.onDestroy()
    }

}
