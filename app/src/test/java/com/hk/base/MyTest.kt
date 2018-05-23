package com.hk.base

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import kotlin.concurrent.thread

/**
 * Created by 123 on 2017/12/8.
 */
class MyTest {
    @Test fun test(){
        val num = CyclicBarrier (5)
        val countDownLatch = CountDownLatch(5)
        for (i in 0 until 5){
            thread {
                try {
                    println("hello")
                    num.await()
                    println("world")
                    countDownLatch.countDown()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
        }
        countDownLatch.await()
        assertTrue(true)
    }
}