package dev.kxxcn.app_with

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

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
}
