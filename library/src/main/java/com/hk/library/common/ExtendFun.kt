package com.hk.library.common

import android.support.design.widget.TabLayout
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.hk.library.ui.BaseActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2018/3/21.
 */
fun WebView.setMobileStyle() {
    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    settings.javaScriptEnabled = true
    settings.loadsImagesAutomatically = true
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true
    settings.domStorageEnabled = true
    settings.cacheMode = WebSettings.LOAD_NO_CACHE
    settings.setSupportZoom(true)
    settings.builtInZoomControls = true
    settings.displayZoomControls = false
}
/**
 * 查看textview是否出现省略号
 */
fun TextView.isEllipsis(): Boolean{
    if (lineCount < 1) return false
    val ellipsisCount = layout.getEllipsisCount(lineCount -1)
    return ellipsisCount > 0
}

/**
 * rxjava异步简写
 */
fun <T> Flowable<T>.enqueue(onNext: Consumer<in T>, onError: Consumer<in Throwable>){
    subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext,onError)
}
inline fun <reified T: BaseActivity>getActivity():T?{
    BaseActivity.activityList.forEach { if (it is T) return it}
    return null
}
fun saveConfig(key: String,config: Any){
    SPUtils.getInstance().put(key, Gson().toJson(config))
}
fun delConfig(){
    SPUtils.getInstance().clear()
}
inline fun <reified T> getConfig(key: String): T?{
    val json = SPUtils.getInstance().getString(key)
    return if (json.isBlank()) null else Gson().fromJson(json,T::class.java)
}

/**
 * 设置tablayout下划线的宽度
 */
fun TabLayout.setIndicator(leftDip: Int, rightDip: Int){
    val tabClass = this.javaClass
    val tabStrip = try {
        tabClass.getDeclaredField("mTabStrip")
    }catch (e: NoSuchFieldException){
        e.printStackTrace()
        null
    }
    tabStrip?.isAccessible = true
    val tabLayout = try {
        tabStrip?.get(this) as LinearLayout
    } catch (e: IllegalAccessError){
        e.printStackTrace()
        null
    }
    val left = ConvertUtils.dp2px(leftDip.toFloat())
    val right = ConvertUtils.dp2px(rightDip.toFloat())
    for ( i in 0 until (tabLayout?.childCount ?: 0)){
        val child = tabLayout?.getChildAt(i)
        child?.setPadding(0,0,0,0)
        val params = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1f)
        params.leftMargin = left
        params.rightMargin = right
        child?.layoutParams = params
        child?.invalidate()
    }
}
