package com.hk.library.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.LogUtils
import com.hk.library.R

/**
 * Created by 23641 on 2017/9/18.
 */
class ExtendSearchView : SearchView {
    var isShowSearchIcon = true
    private val mSearchSrcTextView:SearchAutoComplete = findViewById(R.id.search_src_text)
    private val mSearchButton = findViewById<ImageView>(R.id.search_button)
    private val mCollapsedIcon = findViewById<ImageView>(R.id.search_mag_icon)
    private val mSearchEditFrame = findViewById<View>(R.id.search_edit_frame)
    private val mSearchBadge = findViewById<View>(R.id.search_badge)
    private val mSearchPlate = findViewById<View>(R.id.search_plate)
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context,attrs){
        val a = context.obtainStyledAttributes(attrs, R.styleable.ExtendSearchView)
        mSearchSrcTextView.setTextColor(a.getColor(R.styleable.ExtendSearchView_android_textColor,Color.BLACK))
        mSearchSrcTextView.setHintTextColor(a.getColor(R.styleable.ExtendSearchView_android_textColorHint,Color.BLACK))
        val textSize = a.getDimension(R.styleable.ExtendSearchView_android_textSize,12f)
        mSearchSrcTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        val hint = a.getString(R.styleable.SearchView_queryHint)
        mSearchSrcTextView.hint = hint
        isShowSearchIcon = a.getBoolean(R.styleable.ExtendSearchView_isShowSearchIcon, true)
        if (!isShowSearchIcon){
            mSearchButton.visibility = View.GONE
            mCollapsedIcon.visibility = View.GONE
        }
        a.recycle()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int): super(context,attrs,
            defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mSearchSrcTextView.setBackgroundDrawable(null)
        mSearchEditFrame.layoutParams = (mSearchEditFrame.layoutParams as LinearLayout.LayoutParams)
                .also { it.setMargins(0,0,0,0) }
    }





}