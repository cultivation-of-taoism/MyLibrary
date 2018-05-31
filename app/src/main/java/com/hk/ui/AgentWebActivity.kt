package com.hk.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.umeng.soexample.R
import com.just.agentweb.AgentWeb
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.umeng.socialize.shareboard.ShareBoardConfig
import kotlinx.android.synthetic.main.activity_agent_web.*

class AgentWebActivity : AppCompatActivity() {
    companion object {
        const val URL = "URL"
    }

    lateinit var web: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true)
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        //HINT 微信分享如果包名和签名不一致的话就会出现分享微信闪退的情况
        //hint 并且不会走分享回调
        UMConfigure.init(this, "59892f08310c9307b60023d0", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "669c30a9584623e70e8cd01b0381dcb4")
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0")
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba")
        setContentView(R.layout.activity_agent_web)
        val url = intent.getStringExtra(URL)
        web = AgentWeb.with(this)
                .setAgentWebParent(frameLayout, FrameLayout.LayoutParams(FrameLayout.LayoutParams
                        .MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .addJavascriptInterface("android",this)
                .createAgentWeb()
                .ready()
                .go(url ?: "http://www.zhengzhouchunchang.com/songshu/Your_investment.html")
    }
    @JavascriptInterface
    fun share(content: String){
        ToastUtils.showShort(content)
        ShareAction(this).withMedia(UMWeb("https://www.baidu.com").also {
            it.title = "百度一下"
            it.setThumb(UMImage(this, R.drawable.umeng_socialize_wechat))
            it.description = "百度一下，你就知道"
        }).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA
                .WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .setCallback(object :UMShareListener{
                    override fun onResult(p0: SHARE_MEDIA?) {
                        ToastUtils.showShort("分享成功！")
                    }

                    override fun onCancel(p0: SHARE_MEDIA?) {
                        ToastUtils.showShort("分享取消！")
                    }

                    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                        ToastUtils.showShort("分享失败！${p1?.message}")
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                        ToastUtils.showShort("分享开始！")
                    }
                }).open(ShareBoardConfig().also { it.setIndicatorVisibility(false) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //不加此句不会触发qq回调
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        web.destroy()
    }

}
