package com.hk.library.presenter



import com.hk.library.retrofit.ApiException
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog

/**
 * Created by Administrator on 2016/12/6.
 */

open class Presenter(protected var view: IView) : IPresenter {
    override fun showProgress(isShow: Boolean) {
        if (isShow) view.loadProgressDialog.show()
        else view.loadProgressDialog.dismiss()
    }

    protected var dialog: LoadProgressDialog = view.loadProgressDialog

    override fun onSuccess(rs: Any, task: Int) {
        view.showSuccess(rs, task)
    }

    @Deprecated("使用onError(error: String, task: Int)替换",
            ReplaceWith("onError(error: String, task: Int)"))
    override fun onError(error: String) {
        view.showError(error)
    }

    override fun onError(error: Any, task: Int) = if (error is ApiException)
        view.showError(error.message , task)
    else
        view.showError(error,task)
}
