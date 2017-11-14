package com.hk.library.view

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView

import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.hk.library.R

/**
 * Created by Administrator on 2016/11/19.
 * 旋转进度加载弹窗
 */

class LoadProgressDialog(context: Context) {
    /*************对话框打开的次数
     * 每请求打开一次次数+1，每请求关闭一次-1，只有在次数为0时，
     * 对话框才真正关闭，防止多个并发请求时，对话框提前关闭,当Activity销毁时，次数为-1，此时不可
     * 打开对话框 */
    var count: Int = 0
    val ad: AlertDialog = AlertDialog.Builder(context, R.style.DialogTranslucent).setView(R
            .layout.load_progress).create()
    private var messageView: TextView? = null
    lateinit var spinView:SpinView
    lateinit var layoutProgress:View
    private var isFirst = true
    var width = ConvertUtils.dp2px(120f)//默认宽度120dp
    var margin = ConvertUtils.dp2px(40f)//默认边距40dp
    var marginCenter = 0//距离手机屏幕中心的距离（垂直方向）
    var isTranslucentBackground = false

    init {
        // TODO Auto-generated constructor stub
        ad.setCanceledOnTouchOutside(false)
        ad.setCancelable(false)
    }

    fun show() {
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        if (count < 0) return
        count++
        LogUtils.v("对话框显示次数:$count")
        if (ad.isShowing) return
        try {
            if (isFirst) {
                ad.show()
                messageView = ad.findViewById(R.id.prompt)
                spinView = ad.findViewById(R.id.spin_view)!!
                layoutProgress = ad.findViewById(R.id.layout_progress)!!
                if (isTranslucentBackground)
                layoutProgress.setBackgroundColor(Color.TRANSPARENT)
                var lp:LinearLayout.LayoutParams = spinView.layoutParams as LinearLayout
                .LayoutParams
                lp.width = width-margin
                lp.height = width-margin
                lp.topMargin = marginCenter
                spinView.layoutParams = lp
                lp = messageView!!.layoutParams as LinearLayout.LayoutParams
                lp.width = width-margin
                messageView!!.layoutParams = lp
                ad.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                isFirst = false
            } else
                ad.show()
        } catch (e: Exception) {
        }

    }

    fun setMessage(message: String) {
        if (messageView != null)
            messageView!!.text = message
    }

    /**
     * 关闭对话框
     */
    fun dismiss() {
        if (count > 0) count--
        LogUtils.v("对话框显示次数：" + count)
        if (ad.isShowing && count == 0)
            ad.dismiss()
    }
}
