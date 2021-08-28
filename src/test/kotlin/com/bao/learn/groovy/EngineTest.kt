package com.bao.learn.groovy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.script.ScriptEngineManager

class EngineTest {
    @Test
    fun mainTest() {
        with(ScriptEngineManager().getEngineByExtension("kts")) {
            eval("val x = 3")
            val res2 = eval("x + 2")
            Assertions.assertEquals(5, res2)
        }
    }
}