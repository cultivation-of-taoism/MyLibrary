package com.hk.library.view

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.View
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
    companion object {
        val confing: Config = Config()
    }
    var count: Int = 0
    val ad: AlertDialog = AlertDialog.Builder(context, R.style.DialogTranslucent).setView(R
            .layout.load_progress).create()
    private var messageView: TextView? = null
    lateinit var spinView:SpinView
    lateinit var layoutProgress:View
    private var isFirst = true
    init {
        // TODO Auto-generated constructor stub
        ad.setCanceledOnTouchOutside(false)
        ad.setCancelable(false)
        ad.window.setDimAmount(confing.dimAmount)
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
                if (!confing.isShowText)messageView?.visibility = View.GONE
                messageView?.setTextColor(confing.textColor)
                messageView?.textSize = confing.textSize
                spinView = ad.findViewById(R.id.spin_view)!!
                layoutProgress = ad.findViewById(R.id.layout_progress)!!
                layoutProgress.setBackgroundResource(confing.background)
                var lp:LinearLayout.LayoutParams = spinView.layoutParams as LinearLayout
                .LayoutParams
                lp.width = confing.width
                lp.height = confing.width
                lp.topMargin = confing.marginCenter
                spinView.layoutParams = lp
                isFirst = false
            } else
                ad.show()
            updateWindow()
        } catch (e: Exception) {
        }

    }

    fun setMessage(message: String) {
        if (messageView != null&&confing.isShowText){
            messageView!!.text = message
            updateWindow()
        }
    }
    private fun updateWindow(){
        layoutProgress.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
        LogUtils.v("width",layoutProgress.measuredWidth,"height",layoutProgress.measuredHeight)
        var size = if (layoutProgress.measuredWidth>layoutProgress.measuredHeight) layoutProgress
                .measuredWidth
        else layoutProgress.measuredHeight
        if (size>ConvertUtils.dp2px(150f)) size = ConvertUtils.dp2px(150f)
        val lp = layoutProgress.layoutParams
        lp.width = size
        lp.height = size
        layoutProgress.layoutParams = lp
        ad.window!!.setLayout(size, size)
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
    class Config(var width: Int = ConvertUtils.dp2px(80f),//默认宽度80dp
                 var marginCenter: Int = 0,//距离手机屏幕中心的距离（垂直方向）
                 var background: Int = R.drawable.shape_white_5,//对话框背景 默认白色
                 var dimAmount: Float = 0.5f,//dialog背景透明度0~1
                 var isShowText:Boolean = true,//是否显示文字
                 var textColor: Int = Color.parseColor("#888888"),//文字颜色
                 var textSize: Float = 12f//文字大小
    )
}
