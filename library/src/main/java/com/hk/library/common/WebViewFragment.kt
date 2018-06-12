package com.hk.library.common


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.hk.library.R
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_web_view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class WebViewFragment : Fragment() {
    lateinit var url: String
    lateinit var agentWeb: AgentWeb
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogUtils.v(url)
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView_container, FrameLayout.LayoutParams(FrameLayout
                        .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(resources.getColor(R.color.colorAccent), 1)
                .createAgentWeb()
                .ready()
                .go(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        agentWeb.webLifeCycle.onDestroy()
    }

}
