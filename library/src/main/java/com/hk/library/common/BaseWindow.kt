package rundi.investmentadviser.window

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.TintContextWrapper
import android.view.View
import android.widget.PopupWindow
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog
import rundi.investmentadviser.common.CommonUtils

/**
 * Created by 23641 on 2017/6/27.
 */
open class BaseWindow
    :PopupWindow , PopupWindow.OnDismissListener , IView{
    override val iiView: View = contentView
    override val mContext: Context = iiView.context
    protected val activity: BaseActivity = when (mContext) {
        is TintContextWrapper -> (mContext as TintContextWrapper).baseContext as BaseActivity
        else -> (mContext as BaseActivity)
    }
    override var loadProgressDialog: LoadProgressDialog = activity.loadProgressDialog

    override fun showError(error: Any, task: Int) {
        showError(error.toString())
    }

    override fun showError(error: String) {
        activity.showError(error)
    }

    override fun showSuccess(`object`: Any, task: Int) {

    }

    val context:Context
    open var isChangeDark: Boolean = true
    constructor(id:Int,width:Int,height:Int,context: Context):super(View.inflate(context,id,null),width,height)
    constructor(view: View,width:Int,height:Int):super(view,width,height)
    override fun onDismiss() {
        if (isChangeDark)CommonUtils.setBackgroundAlpha(context as Activity, 1f)
    }
    init {
        isTouchable = true
        isOutsideTouchable = true
        isFocusable = true//使popuwindow之外的控件不可点击
        setBackgroundDrawable(ColorDrawable(0x00000000))
        setOnDismissListener(this)
        setView()
        context = contentView.context
    }

    open fun setView(){

    }
    open fun afterShow(){

    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        if (isChangeDark)CommonUtils.setBackgroundAlpha(context as Activity, 0.5f)
        super.showAtLocation(parent, gravity, x, y)
        afterShow()
    }


    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        if (isChangeDark)CommonUtils.setBackgroundAlpha(context as Activity, 0.5f)
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        afterShow()
    }
}