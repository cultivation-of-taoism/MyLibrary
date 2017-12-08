package com.hk.library.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by 123 on 2017/12/8.
 */
class NotNUllSingleVar<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
            value ?: throw IllegalStateException("还没有被赋值")
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (this.value != null) return
        this.value = value ?: throw IllegalStateException("不能设置为null，或已经有了")
    }
}