package com.hk.library.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

open class FragmentAdapter(val data: List<Fragment>,
                      val containerViewId: Int,
                      val fragmentManager: FragmentManager) {
    private lateinit var currentFragment: Fragment
    fun setCurrentItem(position: Int){
        currentFragment = data[position]
        fragmentManager.beginTransaction()
                .replace(containerViewId, currentFragment)
                .commit()
    }
    fun getCurrentItem(): Fragment{
        currentFragment = fragmentManager.findFragmentById(containerViewId)
        return currentFragment
    }
}