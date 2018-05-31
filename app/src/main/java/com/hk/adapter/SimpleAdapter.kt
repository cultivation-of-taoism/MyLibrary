package com.hk.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.umeng.soexample.R

/**
 * Created by 23641 on 2017/10/25.
 */
class SimpleAdapter:BaseQuickAdapter<Int,BaseViewHolder>(R.layout.item_simple,(0..100)
        .toMutableList()) {
    override fun convert(helper: BaseViewHolder, item: Int) {

    }
}