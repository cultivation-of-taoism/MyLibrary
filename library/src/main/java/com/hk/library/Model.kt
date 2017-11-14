package com.hk.library

import com.google.gson.Gson
import com.hk.library.presenter.IPresenter


/**
 * Created by Administrator on 2016/12/5.
 */

open class Model (protected var callback: IPresenter) {
    protected var gson = Gson()
}
