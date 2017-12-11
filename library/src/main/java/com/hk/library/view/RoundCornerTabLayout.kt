package com.hk.library.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.widget.LinearLayout
import com.hk.library.R
import com.hk.library.R.attr.cornerRadius
import com.hk.library.R.attr.dividerColor

/**
 * Created by 23641 on 2017/9/28.
 */
class RoundCornerTabLayout:TabLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerTabLayout)
        cornerRadius = typedArray.getDimension(R.styleable.RoundCornerTabLayout_cornerRadius,
                cornerRadius)
        borderWith = typedArray.getDimension(R.styleable.RoundCornerTabLayout_borderWidth,0f)
        dividerColor = typedArray.getColor(R.styleable.RoundCornerTabLayout_dividerColor,Color.TRANSPARENT)
        typedArray.recycle()
        //canvas.clipPath()在开启硬件加速的情况下，只有API18以上有效，所以要关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int): super(context,attrs,
            defStyleAttr)

    private lateinit var mRect:RectF
    private lateinit var mInnerRect:RectF
    private lateinit var bgBitmap:Bitmap
    private lateinit var bgCanvas:Canvas
    private val mPath = Path()
    private var cornerRadius = 0f
    private var borderWith = 0f
    private var dividerColor = Color.TRANSPARENT
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDividerPoints = arrayListOf<Float>()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRect = RectF(0f,0f,w.toFloat(),h.toFloat())
        mInnerRect = RectF(borderWith,borderWith,w-borderWith,h-borderWith)
        background.setBounds(0,0,w,h)
        bgBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        bgCanvas = Canvas(bgBitmap)
        mPath.addRoundRect(mRect,cornerRadius,cornerRadius,Path.Direction.CCW)
        mPath.addRoundRect(mInnerRect,cornerRadius,cornerRadius,Path
                .Direction.CCW)
        mPath.fillType = Path.FillType.EVEN_ODD
        bgCanvas.save()
        bgCanvas.clipPath(mPath)
        background.draw(bgCanvas)
        bgCanvas.restore()
        setBackgroundDrawable(BitmapDrawable(resources,bgBitmap))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (tabCount<=1)return
        mDividerPoints.clear()
        val mTabStrip = getChildAt(0) as LinearLayout
        for(i in 0 until tabCount-1){
            val child = mTabStrip.getChildAt(i)
            mDividerPoints.add(child.right.toFloat())
            mDividerPoints.add(child.top.toFloat())
            mDividerPoints.add(child.right.toFloat())
            mDividerPoints.add(child.bottom.toFloat())
        }
    }


    override fun dispatchDraw(canvas: Canvas) {
        mPath.reset()
        mPath.addRoundRect(mInnerRect,cornerRadius,cornerRadius,Path
                .Direction.CW)
        canvas.save()
        canvas.clipPath(mPath)
        super.dispatchDraw(canvas)
        drawDivider(canvas)
        canvas.restore()
    }
    private fun drawDivider(canvas: Canvas){
        if (tabCount<=1)return
        mPaint.color = dividerColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = borderWith
        canvas.drawLines(mDividerPoints.toFloatArray(), mPaint)
    }
}