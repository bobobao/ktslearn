package com.bao.learn.script

import com.bao.learn.script.runner.KtsObjectLoader
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
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

    @Test
    fun `simple evaluations should work`() {
        with(KtsObjectLoader().engine) {
            val res1 = eval("val x = 3")
            Assertions.assertNull(res1)
            val res2 = eval("x + 2")
            Assertions.assertEquals(5, res2)
        }
    }

    @Test
    fun `when loading class from string script the content should be as expected`() {

        val scriptContent = String(Files.readAllBytes(Paths.get("src/test/resources/testscript.kts")))

        val loaded = KtsObjectLoader().load<ClassFromScript>(scriptContent)
        Assertions.assertEquals("content", loaded.x)
    }

    @Test
    fun html() {
    }
}