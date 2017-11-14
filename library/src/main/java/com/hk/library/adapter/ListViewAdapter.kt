package com.hk.library.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog


/**
 * Created by Administrator on 2016/12/5.
 */

abstract class ListViewAdapter: BaseAdapter() {
    protected var layout: Int = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: BaseViewHolder
        if (convertView == null) {
            layout = getLayoutId()
            convertView = View.inflate(parent.context, layout, null)
            holder = getViewHolder(convertView, parent)
            convertView!!.tag = holder
        } else
            holder = convertView.tag as BaseViewHolder
        holder.setData(position)
        return convertView
    }

    abstract fun getLayoutId():Int
    abstract fun getViewHolder(convertView: View, parent: ViewGroup): BaseViewHolder

    /**
     * 要求子类必须重写继承这个内部类类
     */
    open inner class BaseViewHolder(protected var convertView: View, parent: ViewGroup) :
            IView, View.OnClickListener {
        override val contentView: View
            get() = convertView
        protected var position: Int = 0
        protected var context: BaseActivity = parent.context as BaseActivity
        override val mContext: Context = context
        open fun setData(position: Int) {
            this.position = position

        }

        override var loadProgressDialog: LoadProgressDialog = context.loadProgressDialog

        override fun showSuccess(`object`: Any, task: Int) {

        }

        override fun onClick(v: View) {

        }

        override fun showError(error: String) {
            context.showError(error)
        }

        override fun showError(error: Any, task: Int) {
            showError(error.toString())
        }


    }


}
