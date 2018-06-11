package com.hk

import com.hk.library.presenter.Presenter
import com.hk.library.ui.IView

/**
 * Created by 23641 on 2017/10/9.
 */
class ApiPresenter(iView: IView):Presenter(iView) {
    private val model:ApiModel = ApiModel(this)
    fun registerToken(serial: String){
        model.registerToken(serial)
    }
    fun getInformationList(page: Int){
        model.getInformationList(page)
    }
}