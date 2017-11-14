package com.hk.library.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.util.TypedValue
import com.hk.library.R

/**
 * Created by 23641 on 2017/9/18.
 */
class ExtendSearchView : SearchView {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context,attrs){
        val mSearchSrcTextView:SearchAutoComplete = findViewById(R.id.search_src_text)
        var a = context.obtainStyledAttributes(
                attrs, intArrayOf(android.R.attr.textColor))
        mSearchSrcTextView.setTextColor(a.getColor(0,Color.BLACK))
        a.recycle()
        a = context.obtainStyledAttributes(
                attrs, intArrayOf(android.R.attr.textColorHint))
        mSearchSrcTextView.setHintTextColor(a.getColor(0,Color.BLACK))
        a.recycle()
        a = context.obtainStyledAttributes(
                attrs, intArrayOf(android.R.attr.textSize))
        val textSize = a.getDimensionPixelSize(0,12)
        mSearchSrcTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize.toFloat())
        a.recycle()
        a = context.obtainStyledAttributes(
                attrs, R.styleable.SearchView)
        val hint = a.getString(R.styleable.SearchView_queryHint)
        mSearchSrcTextView.hint = hint
        a.recycle()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int): super(context,attrs,
            defStyleAttr)
}