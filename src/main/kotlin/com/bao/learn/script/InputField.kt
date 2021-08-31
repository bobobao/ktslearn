package com.bao.learn.script

import com.bao.learn.script.vo.FieldDefined
import java.math.BigInteger
import java.nio.charset.Charset

interface InputField {
    val defined: FieldDefined
    val inputData: ByteArray

    fun display(): String = inputData.contentToString()

    fun toInt() = BigInteger(inputData).toInt()
}

class FixedInputField(override val defined: FieldDefined, override val inputData: ByteArray) : InputField {
    override fun toString(): String {
        return "FixedInputField(defined=$defined, inputData=${inputData.contentToString()})"
    }
}

class StringInputField(override val defined: FieldDefined, override val inputData: ByteArray) : InputField {
    override fun display(): String = String(inputData, Charset.forName("GBK"))
    override fun toString(): String {
        return "StringInputField(defined=$defined, inputData=${inputData.contentToString()}, display=${display()})"
    }
}