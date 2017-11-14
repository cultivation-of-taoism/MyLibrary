package com.hk.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hk.library.ui.BaseActivity

import com.hk.base.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.bt_hk_spinner->startActivity(Intent(this, HKSpinnerActivity::class.java))
            R.id.bt_round_corner_tab_layout->startActivity(Intent(this,
                    RoundCornerTabLayoutActivity::class.java))
            R.id.bt_mvp->startActivity(Intent(this, MvpActivity::class.java))
            R.id.bt_CollapsingToolbarLayout->startActivity( Intent(this,
                    CollapsingToolbarLayoutActivity::class.java))
            R.id.bt_glide->startActivity(Intent(this, GlideActivity::class.java))
            R.id.bt_search_view->startActivity(Intent(this, SearchViewActivity::class.java))
            R.id.bt_smart_refresh_layout->startActivity(Intent(this, SmartRefreshLayoutActivity::class.java))
            R.id.bt_nested_scroll_view->startActivity(Intent(this, NestedScrollViewActivity::class.java))
        }
    }
}
