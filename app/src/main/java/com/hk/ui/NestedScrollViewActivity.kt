package com.hk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hk.adapter.SimpleAdapter
import com.hk.R
import kotlinx.android.synthetic.main.activity_nested_scroll_view.*

class NestedScrollViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_view)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.layoutManager.isAutoMeasureEnabled = true
        recyclerView.adapter = SimpleAdapter()
    }
}
