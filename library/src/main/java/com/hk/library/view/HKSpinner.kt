package com.hk.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.ListPopupWindow
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Spinner
import com.hk.library.adapter.HKSpinnerAdapter

/**
 * Created by 23641 on 2017/9/29.
 */
class HKSpinner :AppCompatSpinner {
    lateinit var mPopup:ListPopupWindow
    lateinit var mPopup23:android.widget.ListPopupWindow
    lateinit var mDivider: Drawable
    var isUnder23 = false

    private var mDividerHeight: Int = 0
    constructor(context: Context):super(context)

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        var a = context.obtainStyledAttributes(attributeSet,R.styleable.Spinner)
        var entries = a.getTextArray(R.styleable.Spinner_android_entries)
        a.recycle()
        a = context.obtainStyledAttributes(attributeSet,R.styleable.HKSpinner)
        val textSize = a.getDimension(R.styleable.HKSpinner_android_textSize,12f)
        val textColor = a.getColor(R.styleable.HKSpinner_android_textColor,Color.BLACK)
        val textHeight = a.getLayoutDimension(R.styleable.HKSpinner_android_layout_height,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        val textGravity = a.getInt(R.styleable.HKSpinner_android_gravity,Gravity.CENTER)
        val padding = a.getDimensionPixelSize(R.styleable.HKSpinner_android_padding,0)
        var paddingLeft = a.getDimensionPixelSize(R.styleable.HKSpinner_android_paddingLeft,0)
        var paddingRight = a.getDimensionPixelSize(R.styleable.HKSpinner_android_paddingRight,0)
        var paddingTop = a.getDimensionPixelSize(R.styleable.HKSpinner_android_paddingTop,0)
        var paddingBottom = a.getDimensionPixelSize(R.styleable.HKSpinner_android_paddingBottom,0)
        if (padding!=0){
            if (paddingLeft == 0)paddingLeft = padding
            if (paddingRight == 0)paddingRight = padding
            if (paddingTop == 0)paddingTop = padding
            if (paddingBottom == 0)paddingBottom = padding
        }
        triangleMarginRight = a.getDimension(R.styleable.HKSpinner_triangleMarginRight,0f)
        triangleMarginVertical = a.getDimension(R.styleable.HKSpinner_triangleMarginVertical,0f)
        triangleColor = a.getColor(R.styleable.HKSpinner_triangleColor,Color.BLACK)
        mDivider = a.getDrawable(R.styleable.HKSpinner_android_divider)
        mDividerHeight = a.getDimensionPixelSize(R.styleable.HKSpinner_android_dividerHeight,0)
        a.recycle()
        if (entries == null) entries = arrayOf()
        spinnerAdapter = HKSpinnerAdapter<CharSequence>(
                context, android.R.layout.simple_spinner_item, entries, textSize, textColor,
                textHeight, textGravity)
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerAdapter.paddingLeft = paddingLeft
        spinnerAdapter.paddingBottom = paddingBottom
        spinnerAdapter.paddingTop = paddingTop
        spinnerAdapter.paddingRight = paddingRight
        adapter = spinnerAdapter
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode){
            isUnder23 = Build.VERSION.SDK_INT<23
            val field = if (isUnder23) {
                AppCompatSpinner::class.java.getDeclaredField("mPopup")

            }else{
                Spinner::class.java.getDeclaredField("mPopup")
            }
            field.isAccessible = true
            if (isUnder23)
                mPopup = field.get(this) as ListPopupWindow
            else mPopup23 = field.get(this) as android.widget.ListPopupWindow
            field.isAccessible = false
            setListView()
        }
    }


    private var triangleColor:Int = Color.BLACK
    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path:Path = Path()
    private var triangleMarginRight = 0f
    private var triangleMarginVertical = 0f
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
            with(mPopup23.listView!!){
                divider = mDivider
                dividerHeight = mDividerHeight
            }
        }

    }

    override fun performClick(): Boolean {
        setListView()
        return super.performClick()
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val triangleHeight = h-triangleMarginVertical*2
        val triangleLength = triangleHeight/Math.sin(Math.toRadians(50.0)).toFloat()
        val triangleBottom = triangleLength * Math.cos(Math.toRadians(50.0)).toFloat()
        path.moveTo(w-triangleBottom*2-triangleMarginRight,triangleMarginVertical)
        path.lineTo(w-triangleMarginRight,triangleMarginVertical)
        path.lineTo(w-triangleBottom-triangleMarginRight,h.toFloat()-triangleMarginVertical)
        path.close()
        setPadding(0,0,0,0)
        spinnerAdapter.paddingRight = paddingRight+triangleBottom.toInt()*2
        spinnerAdapter.notifyDataSetChanged()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        paint.color = triangleColor
        canvas.drawPath(path,paint)

    }
    fun setData(data:List<CharSequence>){
        spinnerAdapter.clear()
        spinnerAdapter.addAll(data)
        spinnerAdapter.notifyDataSetChanged()
    }
}