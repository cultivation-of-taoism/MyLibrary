package com.hk.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.hk.R
import com.hk.library.common.GlideApp
import kotlinx.android.synthetic.main.activity_glide.*

class GlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)
        GlideApp.with(this)
                .load(R.mipmap.a_1)
                .transform(MultiTransformation(CenterInside(),RoundedCorners(20)))
                .into(image_view)
    }
}
