package io.github.copylibs.bsh

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import bsh.Interpreter
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BshTest {
    @Test
    fun ctx_test() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("io.github.copylibs.bsh", appContext.packageName)
    }

    @Test
    fun eval_test() {
        val interpreter = Interpreter()
        val value = interpreter.eval("(float) 6")
        assertEquals(6f, value)
    }

    @Test
    fun array_test() {
        val interpreter = Interpreter()
        val value = interpreter.eval("new int[] { 1, 2, 3 }")
        assertTrue(value is IntArray);
        val expected = intArrayOf(1, 2, 3);
        assertArrayEquals(expected, value as IntArray);
    }
}
