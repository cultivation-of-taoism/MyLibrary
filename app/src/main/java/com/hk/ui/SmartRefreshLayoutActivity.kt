package com.hk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hk.adapter.SimpleAdapter

import com.umeng.soexample.R
import kotlinx.android.synthetic.main.activity_smart_refresh_layout.*

class SmartRefreshLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_refresh_layout)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SimpleAdapter()
        toolbar.setNavigationOnClickListener { finish() }
    }
}
