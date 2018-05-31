package rundi.investmentadviser.common

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager
import android.view.WindowManager


import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.StringUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

import java.io.File
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.Calendar

/**
 * Created by Administrator on 2016/11/16.
 * 常用的工具方法
 */

object CommonUtils {
    /**
     * 设置添加popuWindow屏幕的背景透明度
     * @param bgAlpha
     */
    fun setBackgroundAlpha(context: Activity, bgAlpha: Float) {
        val lp = context.window.attributes
        lp.alpha = bgAlpha
        context.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        context.window.attributes = lp
    }

    /**
     * 以友好的方式显示时间
     * @param
     * @return
     */
    fun friendlyTime(mills: Long): String {
        if (mills == 0L) {
            return "Unknown"
        }
        val s_time: String
        //获取当前时间
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)
        calendar.timeInMillis = mills
        val beforeMinute = calendar.get(Calendar.MINUTE)
        val beforeSecond = calendar.get(Calendar.SECOND)
        val minute: Int = currentMinute - beforeMinute
        val second: Int = currentSecond - beforeSecond
        s_time = when {
            minute > 0 -> minute.toString() + "分钟前"
            second > 0 -> second.toString() + "秒前"
            else -> "0秒前"
        }
        return s_time
    }

    fun getFileDir(context: Context): String {
        val dir: File = if (SDCardUtils.isSDCardEnable()) {
            context.getExternalFilesDir(null)
        } else {
            context.filesDir
        }
        return dir.absolutePath
    }

    fun getDownLoadDir(context: Context): String =
            getFileDir(context) + File.separator + Environment.DIRECTORY_DOWNLOADS

    fun getAppPubicDir(appDir: String): String? {
        var dir = SDCardUtils.getSDCardPaths(true)
        if (dir.isEmpty()) dir = SDCardUtils.getSDCardPaths(false)
        if (dir.isEmpty()) return null
        val file = File(dir.first(), appDir)
        if (!file.exists()) file.mkdir()
        return file.absolutePath
    }


}
