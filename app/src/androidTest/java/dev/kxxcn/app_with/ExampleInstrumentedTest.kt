package dev.kxxcn.app_with

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun splitTest() {
        val str = "2019-07-23"
        val array = str.split("-", limit = 2)
        println(array)
    }

    @Test
    fun gcTest() {
        val weakRefs = LinkedList<WeakReference<BigData>>()
        val softRefs = LinkedList<SoftReference<BigData>>()
        val strongRefs = LinkedList<BigData>()
        try {
            while (true) {
                // weakRefs.add(WeakReference(BigData()))
                // softRefs.add(SoftReference(BigData()))
                strongRefs.add(BigData())
            }
        } catch (e: OutOfMemoryError) {
            println("OutOfMemoryError!")
        }
    }
}

class BigData {
    private val array = IntArray(2500)
}
