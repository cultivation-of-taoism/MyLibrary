package com.hk.base

import com.hk.library.delegate.NotNUllSingleVar
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        data class Person(val age: Int)
        var person: Person by NotNUllSingleVar()
        person = Person(1)
        System.out.println(person)
        person = Person(2)
        System.out.println(person)
        assertEquals(4, (2 + 2).toLong())
    }
}