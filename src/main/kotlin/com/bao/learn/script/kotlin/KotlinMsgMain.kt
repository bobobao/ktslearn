package com.bao.learn.script.kotlin

import com.bao.learn.script.kotlin.runner.KtsObjectLoader
import com.bao.learn.script.kotlin.vo.FlexibleFieldDefined
import com.bao.learn.script.kotlin.vo.MessageDefined
import io.netty.buffer.Unpooled
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val scriptContent = String(Files.readAllBytes(Paths.get("src/test/resources/testscript.kts")))

    val messageDefined = KtsObjectLoader().load<MessageDefined>(scriptContent)

    val byteBuf = Unpooled.copiedBuffer(
        byteArrayOf(
            0x01,
            0x01,
            0x02,
            0x01,
            0x02,
            0x03,
            0x04,
            0x04,
            *"你好".toByteArray(Charset.forName("GBK"))
        )
    )

    val inputData = mutableMapOf<Int, InputField>()

    messageDefined.fields.forEach {
        when (it) {
            is FlexibleFieldDefined -> {
                val l = inputData[it.lengthField.index]?.toInt() ?: throw RuntimeException()
                val holder = ByteArray(l)
                byteBuf.readBytes(holder)
                inputData[it.index] = StringInputField(it, holder)
            }
            else -> {
                val holder = ByteArray(it.getLength())
                byteBuf.readBytes(holder)
                inputData[it.index] = FixedInputField(it, holder)
            }
        }
    }
    println(inputData)
}