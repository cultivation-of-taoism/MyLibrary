package com.hk.library.ui

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.hk.library.view.LoadProgressDialog

/**
 * Created by Administrator on 2016/11/28.
 */

open class BaseActivity : AppCompatActivity(), IView, View.OnClickListener {
    override val lifecycleOwner: LifecycleOwner
        get() = this
    override val iiView: View
        get() = window.decorView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityList.add(this)
        loadProgressDialog = LoadProgressDialog(this)
    }

    open fun setView() {}

    override fun onDestroy() {
        activityList.remove(this)
        loadProgressDialog.dismiss()
        loadProgressDialog.count = -1
        super.onDestroy()
    }

    override fun showSuccess(`object`: Any, task:Int) {}

    override fun showError(error: String) {
        if (error.isNotBlank())
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: Any, task:Int) {
        showError(error.toString())
    }

    override val mContext: Context = this

    override lateinit var loadProgressDialog: LoadProgressDialog

    override fun onClick(v: View) {}
    protected fun hideTitle() {

    }

    open fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.showSoftInput(getWindow().getDecorView(),InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0) //强制隐藏键盘

    }

    companion object {
        var activityList: MutableList<Activity> = arrayListOf()
        var backPressTime = System.currentTimeMillis()
        fun closeAll(){
            for (activity in activityList) {
                activity.finish()
            }
        }
    }

    /**
     * 退出程序
     */
    open fun exitApp() {
        val time = System.currentTimeMillis()
        if (time - backPressTime > 2000) {
            backPressTime = time
            showError("再按一次退出程序")
        }else closeAll()
    }
}
