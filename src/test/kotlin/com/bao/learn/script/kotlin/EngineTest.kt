package com.bao.learn.script.kotlin

import com.bao.learn.script.kotlin.runner.KtsObjectLoader
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import javax.script.ScriptContext
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

    // https://github.com/JetBrains/kotlin/blob/master/libraries/examples/kotlin-jsr223-local-example/src/test/kotlin/org/jetbrains/kotlin/script/jsr223/KotlinJsr223ScriptEngineIT.kt
    // https://youtrack.jetbrains.com/issue/KT-15125
    @Test
    fun testMultipleCompilable() {
        val engine = ScriptEngineManager().getEngineByExtension("kts") as KotlinJsr223JvmLocalScriptEngine
        val compiled1 = engine.compile("""listOf(1,2,3).joinToString(",")""")
        val compiled2 = engine.compile("""val x = bindings["boundValue"] as Int + bindings["z"] as Int""")
        val compiled3 = engine.compile("""x""")

        Assert.assertEquals("1,2,3", compiled1.eval())
        Assert.assertEquals("1,2,3", compiled1.eval())
        Assert.assertEquals("1,2,3", compiled1.eval())
        Assert.assertEquals("1,2,3", compiled1.eval())

        engine.getBindings(ScriptContext.ENGINE_SCOPE).apply {
            put("boundValue", 100)
            put("z", 33)
        }

        compiled2.eval()

        Assert.assertEquals(133, compiled3.eval())
        Assert.assertEquals(133, compiled3.eval())
        Assert.assertEquals(133, compiled3.eval())
    }
}