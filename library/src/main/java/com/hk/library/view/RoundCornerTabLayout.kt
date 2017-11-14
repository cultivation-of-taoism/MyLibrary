package com.hk.library.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import com.hk.library.R

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
        if (tabCount<=0)return
        mPaint.color = dividerColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = borderWith
        val interval = width/tabCount.toFloat()
        (1 until tabCount).forEach {
            canvas.drawLine(it*interval,0f,it*interval,height.toFloat(),mPaint)
        }
    }
}