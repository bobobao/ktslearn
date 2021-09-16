package com.bao.learn.script.kotlin.vo

import java.nio.charset.Charset

interface FieldDefined {
    val description: String

    val index: Int

    fun getLength(): Int = -1
}

interface FlexibleFieldDefined : FieldDefined {
    val lengthField: FieldDefined
}

class ByteField(
    override val description: String,
    override val index: Int
) : FieldDefined {
    override fun getLength(): Int = 1
    override fun toString(): String {
        return "ByteField(description='$description', index=$index)"
    }
}

class WordField(
    override val description: String,
    override val index: Int
) : FieldDefined {
    override fun getLength(): Int = 2
    override fun toString(): String {
        return "ByteField(description='$description', index=$index)"
    }
}

class DWordField(
    override val description: String,
    override val index: Int
) : FieldDefined {
    override fun getLength(): Int = 4
    override fun toString(): String {
        return "ByteField(description='$description', index=$index)"
    }
}

class BCDArray(
    override val description: String,
    override val index: Int,
    length: Int
) : FieldDefined {
    private val _length = length

    override fun getLength(): Int {
        return _length
    }

    override fun toString(): String {
        return "BCDArray(description='$description', index=$index, _length=$_length)"
    }
}

class StringField(
    override val description: String,
    override val index: Int,
    override val lengthField: FieldDefined,
    val charset: Charset = Charset.forName("GBK")
) : FlexibleFieldDefined {
    override fun toString(): String {
        return "StringField(description='$description', index=$index, lengthField=$lengthField, charset=$charset)"
    }
}