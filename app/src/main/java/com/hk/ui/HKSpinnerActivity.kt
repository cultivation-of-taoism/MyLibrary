package com.hk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.hk.base.R
import kotlinx.android.synthetic.main.activity_hkspinner.*

class HKSpinnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hkspinner)
        spinner_hk.setData(arrayListOf("1","222222222","3","1","222222222","3"))
    }
}
