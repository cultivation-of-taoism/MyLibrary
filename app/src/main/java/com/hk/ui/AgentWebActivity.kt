package com.hk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.hk.base.R
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_agent_web.*

class AgentWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_web)
        AgentWeb.with(this)
                .setAgentWebParent(frameLayout, FrameLayout.LayoutParams(FrameLayout.LayoutParams
                        .MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("http://www.zhengzhouchunchang.com/songshu/Your_investment.html")
    }
}
