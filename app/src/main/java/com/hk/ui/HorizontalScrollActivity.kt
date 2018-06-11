package com.hk.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.hk.R
import com.hk.fragment.HorizontalScrollFragment
import kotlinx.android.synthetic.main.activity_horizontal_scroll.*

class HorizontalScrollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll)
        vp.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getCount(): Int = 2

            override fun getItem(position: Int): Fragment = HorizontalScrollFragment()

            override fun getPageTitle(position: Int): CharSequence {
                return when(position){
                    0->"第一个"
                    1->"第二个"
                    else->""
                }
            }

        }
        tl.setupWithViewPager(vp)
    }
}
