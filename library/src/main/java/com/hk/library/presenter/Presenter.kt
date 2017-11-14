package com.hk.library.presenter



import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog

/**
 * Created by Administrator on 2016/12/6.
 */

open class Presenter(protected var view: IView) : IPresenter {
    protected var dialog: LoadProgressDialog = view.loadProgressDialog

    override fun onSuccess(rs: Any, task: Int) {
        dialog.setMessage("请稍候...")
        dialog.dismiss()
        view.showSuccess(rs, task)
    }

    @Deprecated("使用onError(error: String, task: Int)替换")
    override fun onError(error: String) {
        dialog.setMessage("请稍候...")
        dialog.dismiss()
        view.showError(error)
    }

    override fun onError(error: Any, task: Int) {
        dialog.setMessage("请稍候...")
        dialog.dismiss()
        view.showError(error,task)
    }
}
