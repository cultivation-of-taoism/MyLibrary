package com.hk

import android.content.Context
import com.hk.library.BaseApp
import android.support.multidex.*


/**
 * Created by 23641 on 2017/10/9.
 */
class HKApp: BaseApp() {
    companion object {
        val TAG = "HK"
        val URL_BASE = "http://118.89.188.132/"
        const val URL_REGISTER_TOKEN = "api?method=token.register&version=10"
        const val URL_BASE_1 = "http://tzg.homejia.club/HKoption/"
        const val URL_INFORMATION = "main/getNewsTable"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}