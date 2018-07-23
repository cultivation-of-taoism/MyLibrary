package com.hk.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.hk.R
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_flow_layout.*

class FlowLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout)
        layout_flow.adapter = FlowAdapter(
                listOf(
                        "窗",
                        "春意",
                        "罗衾不",
                        "丁总威武霸气吊炸天",
                        "梦",
                        "独自莫",
                        "一响"
                )
        )
    }

    class FlowAdapter(data: List<String>): TagAdapter<String>(data){
        override fun getView(parent: FlowLayout, position: Int, t: String): View {
            val layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_flow, parent, false)
                    as LinearLayout
            t.toCharArray().forEach {
                val textView = View.inflate(parent.context, R.layout.item_1, null)
                        as TextView
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.setMargins(2,2,2,2)
                textView.layoutParams = lp
                textView.text = it.toString()
                layout.addView(textView)
            }

            return layout
        }
    }
}
