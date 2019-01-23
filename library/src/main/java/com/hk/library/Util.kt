package com.hk.library

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.hk.library.presenter.Presenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * Created by 23641 on 2017/6/15.
 */
const val REQUEST_LOCATION = 9999
const val REQUEST_OVERLAY = 4444
@SuppressLint("CheckResult")
fun Presenter.verificationInterval(tvVerify:TextView, enableColor:Int,
                                   disEnableColor:Int){
    Observable.interval(0, 1,TimeUnit.SECONDS).take(60)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                val int = 60 - it
                tvVerify.text = "重新发送($int s)"
                tvVerify.isEnabled = false
                tvVerify.setTextColor(disEnableColor)
            }, {}, {
                tvVerify.text = "重新发送"
                tvVerify.isEnabled = true
                tvVerify.setTextColor(enableColor)
            })

}


fun Activity.isLocationEnabled(): Boolean{
    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    println("所有定位服务->${lm.allProviders}")
    println("可用定位服务->${lm.getProviders(true)}")
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

/**
 * 显示定位服务未开启确认对话框
 */
fun Activity.showLocServiceDialog() {
    AlertDialog.Builder(this).setTitle("手机未开启位置服务")
            .setMessage("请在 设置-位置信息 (将位置服务打开))")
            .setNegativeButton("取消", null)
            .setPositiveButton("去设置") { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                try {
                    startActivityForResult(intent, REQUEST_LOCATION)
                } catch (ex: ActivityNotFoundException) {
                    intent.action = Settings.ACTION_SETTINGS
                    try {
                        startActivityForResult(intent, REQUEST_LOCATION)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }.show()
}
fun Activity.checkOverlayPermission(): Boolean{
    requestOverlayPermission {  }
    return if (Build.VERSION.SDK_INT >= 23)
        Settings.canDrawOverlays(this)
    else true
}

/**
 * 注意：在悬浮窗权限操作完成后的onActivityResult中需要如下操作
 *
 *     if (requestCode == REQUEST_OVERLAY){
 *           Handler().postDelayed({
 *              if (checkOverlayPermission())
 *                  loadDataWithPermissionCheck()
 *              else{
 *                  LogUtils.v("悬浮窗权限被拒绝！")
 *                  onPermissionDenied()
 *              }
 *          },500)
 *     }
 *
 */
fun Activity.requestOverlayPermission(onPermissionDenied:()->Unit){
    if (Build.VERSION.SDK_INT >= 23) {
        if (!Settings.canDrawOverlays(this)) {
            AlertDialog.Builder(this)
                    .setMessage("是否允许悬浮窗权限？")
                    .setPositiveButton("确定"){ _: DialogInterface, _: Int ->
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        intent.data = Uri.parse("package:$packageName")
                        startActivityForResult(intent, REQUEST_OVERLAY)
                    }
                    .setCancelable(false)
                    .setNegativeButton("取消"){_,_->
                        onPermissionDenied()
                    }.show()

        } else {

        }
    }
}
