package com.hk.adapter

import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hk.R
import com.hk.library.common.GlideApp
import com.lingniu.vestbag.entity.Information
import kotlinx.android.synthetic.main.item_news.view.*

/**
 * Created by Administrator on 2018/3/20.
 */
class NewsAdapter(data:MutableList<Information>) : BaseQuickAdapter<Information, BaseViewHolder>
(R.layout.item_news, data) {
    override fun convert(helper: BaseViewHolder, item: Information) {
        helper.itemView.run {
            content.text = item.title
            tv_date.text = TimeUtils.millis2String(item.createTime)
            GlideApp.with(this)
                    .load(item.imgUrl)
                    .placeholder(R.drawable.load_fail)
                    .error(R.drawable.load_fail)
                    .into(iv_news)
        }
    }


}