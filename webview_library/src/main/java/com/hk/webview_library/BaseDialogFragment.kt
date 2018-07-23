package com.lingniu.shiyingguoji

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment


/**
 * Created by 23641 on 2017/9/11.
 */
open class BaseDialogFragment: DialogFragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog_MinWidth)
        super.onCreate(savedInstanceState)
    }
    fun customWidthAndHeight(width: Int, height: Int){
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(width, height)
    }
}