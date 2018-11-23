package com.hk.library.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.ListPopupWindow
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Spinner
import com.blankj.utilcode.util.LogUtils
import com.hk.library.R
import com.hk.library.adapter.HKSpinnerAdapter

/**
 * Created by 23641 on 2017/9/29.
 */
class HKSpinner :AppCompatSpinner {
    lateinit var mPopup:ListPopupWindow
    lateinit var mPopup23:android.widget.ListPopupWindow
    var mDivider: Drawable? = null
    var mArrowDrawable: BitmapDrawable? = null
    var degrees = 0f
    lateinit var valueAnimator:ValueAnimator
    var isUnder23 = false
    var isOpen = false
    var px = 0f
    var py = 0f

    private var mDividerHeight: Int = 0
    constructor(context: Context):super(context)

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        var a = context.obtainStyledAttributes(attributeSet, R.styleable.Spinner)
        var entries = a.getTextArray(R.styleable.Spinner_android_entries)
        a.recycle()
        a = context.obtainStyledAttributes(attributeSet, R.styleable.HKSpinner)
        val textSize = a.getDimension(R.styleable.HKSpinner_android_textSize,12f)
        val textColor = a.getColor(R.styleable.HKSpinner_android_textColor,Color.BLACK)
        val textHeight = a.getLayoutDimension(R.styleable.HKSpinner_android_layout_height,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        val textGravity = a.getInt(R.styleable.HKSpinner_android_gravity,Gravity.CENTER)
        arrowPadding = a.getDimension(R.styleable.HKSpinner_arrowPadding,0f)
        arrowMarginVertical = a.getDimension(R.styleable.HKSpinner_arrowMarginVertical,0f)
        mArrowDrawable = a.getDrawable(R.styleable.HKSpinner_arrowDrawable) as? BitmapDrawable?
        mDivider = a.getDrawable(R.styleable.HKSpinner_android_divider)
        mDividerHeight = a.getDimensionPixelSize(R.styleable.HKSpinner_android_dividerHeight,0)
        a.recycle()
        if (entries == null) entries = arrayOf()
        spinnerAdapter = HKSpinnerAdapter<CharSequence>(
                context, android.R.layout.simple_spinner_item, entries, textSize, textColor,
                textHeight, textGravity)
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        adapter = spinnerAdapter
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.AT_MOST){
            setMeasuredDimension(measuredWidth + computeArrowWidth(measuredHeight) +
                    arrowPadding.toInt() + spinnerAdapter.paddingLeft + spinnerAdapter.paddingRight,
                    measuredHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        getChildAt(0)?.run {
            layout(0,top,this@HKSpinner.measuredWidth - computeArrowWidth(measuredHeight) -
                    arrowPadding.toInt() - spinnerAdapter.paddingRight, bottom)
        }
    }



    override fun onFinishInflate() {
        super.onFinishInflate()
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        if (!isInEditMode){
            isUnder23 = Build.VERSION.SDK_INT < 23
            val field = if (isUnder23) {
                AppCompatSpinner::class.java.getDeclaredField("mPopup")

            }else{
                Spinner::class.java.getDeclaredField("mPopup")
            }
            field.isAccessible = true
            if (isUnder23){
                mPopup = field.get(this) as ListPopupWindow
            }
            else{
                mPopup23 = field.get(this) as android.widget.ListPopupWindow
            }
            field.isAccessible = false
            setListView()
        }
    }


    private var arrowPadding = 0f
    private var arrowMarginVertical = 0f
    lateinit var spinnerAdapter:HKSpinnerAdapter<CharSequence>
    private fun setListView(){
        val method = if (isUnder23) ListPopupWindow::class.java.getDeclaredMethod("buildDropDown")
        else android.widget.ListPopupWindow::class.java.getDeclaredMethod("buildDropDown")
        if (isUnder23) {
            if (mPopup.listView == null) {
                method.isAccessible = true
                method.invoke(mPopup)
                method.isAccessible = false
            }
            mPopup.verticalOffset = measuredHeight
            with(mPopup.listView!!) {
                divider = mDivider
                dividerHeight = mDividerHeight
            }
        }else{
            if (mPopup23.listView == null){
                method.isAccessible = true
                method.invoke(mPopup23)
                method.isAccessible = false
            }
            mPopup23.verticalOffset = measuredHeight
            with(mPopup23.listView!!){
                divider = mDivider
                dividerHeight = mDividerHeight
            }
        }

    }

    override fun performClick(): Boolean {
        setListView()
        if (!isOpen){
            isOpen = true
            startAnimation()
        }
        val rs = super.performClick()

        var field = if (isUnder23) ListPopupWindow::class.java.getDeclaredField("mPopup")
        else android.widget.ListPopupWindow::class.java.getDeclaredField("mPopup")
        field.isAccessible = true
        val pw = field.get(if (isUnder23) mPopup else mPopup23)
        field.isAccessible = false
        field = PopupWindow::class.java.getDeclaredField("mOnDismissListener")
        field.isAccessible = true
        val onDismissListener = field.get(pw) as PopupWindow.OnDismissListener
        field.isAccessible = false
        if (isUnder23){
            mPopup.setOnDismissListener {
                onDismissListener.onDismiss()
                isOpen = false
                startAnimation()
            }
        }else mPopup23.setOnDismissListener {
            onDismissListener.onDismiss()
            isOpen = false
            startAnimation()
        }
        return rs
    }

    private fun startAnimation() {
        valueAnimator = if (isOpen)ValueAnimator.ofFloat(0f, 180f)
        else ValueAnimator.ofFloat(180f, 0f)
        valueAnimator.duration = 250
        valueAnimator.addUpdateListener {
            degrees = it.animatedValue as Float
            postInvalidate()
        }
        valueAnimator.start()
    }

    private fun computeArrowWidth(height: Int): Int{
        if (mArrowDrawable == null)return 0
        if (mArrowDrawable!!.minimumHeight == 0)return 0
        return ((height - arrowMarginVertical*2)/mArrowDrawable!!.minimumHeight*mArrowDrawable!!
        .minimumWidth).toInt()
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mArrowDrawable == null)return
        val triangleHeight = h-arrowMarginVertical*2//要绘制的三角形的高度
        val drawableHeight = mArrowDrawable!!.minimumHeight//原图的高度
        val scale = triangleHeight/drawableHeight//绘制比例
        val triangleWidth = mArrowDrawable!!.minimumWidth*scale
        val right = w - spinnerAdapter.paddingRight
        val bottom = (h - arrowMarginVertical).toInt()
        val left = (right - triangleWidth).toInt()
        val top = arrowMarginVertical.toInt()
        px = left + (right - left)/2f
        py = h/2f
        mArrowDrawable!!.setBounds(left, top, right, bottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(0, 0, 0, 0)
        spinnerAdapter.paddingLeft = left
        spinnerAdapter.paddingBottom = bottom
        spinnerAdapter.paddingTop = top
        spinnerAdapter.paddingRight = right
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mArrowDrawable == null)return
        canvas.save()
        canvas.rotate(degrees, px, py)
        mArrowDrawable!!.draw(canvas)
        canvas.restore()
    }
    fun setData(data:List<CharSequence>){
        spinnerAdapter.clear()
        spinnerAdapter.addAll(data)
        spinnerAdapter.notifyDataSetChanged()
    }
    fun setText(string: String){
        spinnerAdapter.spinnerText = string
        spinnerAdapter.notifyDataSetChanged()
    }
}