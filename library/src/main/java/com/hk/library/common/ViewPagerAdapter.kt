package com.hk.library.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(private val fragmentList: List<Fragment>, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {
    lateinit var titleList: List<String>
    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence = titleList[position]
}