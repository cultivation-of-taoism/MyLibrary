package com.hk

import com.hk.library.BaseApp


/**
 * Created by 23641 on 2017/10/9.
 */
class HKApp: BaseApp() {
    companion object {
        val TAG = "HK"
        val URL_BASE = "http://118.89.188.132/"
        const val URL_REGISTER_TOKEN = "api?method=token.register&version=10"
    }


}