package com.hk.library

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * Created by 23641 on 2017/9/29.
 */
open class  BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}