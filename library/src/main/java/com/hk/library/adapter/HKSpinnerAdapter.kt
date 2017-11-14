package com.hk.library.adapter

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by 23641 on 2017/9/29.
 */
class HKSpinnerAdapter<T>(context: Context, resource: Int, objects: Array<T>, private val
textSize: Float, private val textColor: Int, private val textHeight: Int,private val textGravity:
Int) :
        ArrayAdapter<T>(context, resource, objects.asList().toMutableList()) {
    var paddingRight:Int = 0
    var paddingLeft:Int = 0
    var paddingTop:Int = 0
    var paddingBottom = 0
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = super.getView(position, convertView, parent)
        val textView:TextView = itemView.findViewById(android.R.id.text1)
        setTextStyle(textView)
        return itemView
    }
    private fun setTextStyle(textView: TextView){
        val lp:ViewGroup.LayoutParams = textView.layoutParams as ViewGroup.LayoutParams
        lp.height = textHeight
        textView.layoutParams = lp
        textView.setTextColor(textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize)
        textView.gravity = textGravity
        textView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = super.getDropDownView(position, convertView, parent)
        val textView:TextView = itemView.findViewById(android.R.id.text1)
        setTextStyle(textView)
        return itemView
    }
}