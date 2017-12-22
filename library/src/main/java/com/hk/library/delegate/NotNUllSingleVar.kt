package com.hk.library.delegate

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by 123 on 2017/12/8.
 */
class NotNUllSingleVar<out T>(private val getValue: ()->T) : ReadOnlyProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) value = getValue()
        return value ?: throw IllegalStateException("还没有被赋值")
    }


}