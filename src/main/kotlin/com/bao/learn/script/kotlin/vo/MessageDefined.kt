package com.bao.learn.script.kotlin.vo

import java.nio.charset.Charset

class MessageDefined(
    var msgId: Int? = null,
    var name: String? = null,
    var description: String? = null,
    val fields: MutableList<FieldDefined> = mutableListOf()
) : FieldsHolder {
    override fun addField(field: FieldDefined) {
        fields.add(field)
    }
}

fun msg(
    id: Int,
    name: String = "",
    description: String = "",
    block: MessageDefined.() -> Unit
): MessageDefined {
    val msg = MessageDefined(id, name, description)
    msg.block()
    return msg
}

interface FieldsHolder {
    fun addField(field: FieldDefined)
}

fun MessageDefined.fields(block: FieldsHolder.() -> Unit) {
    this.block()
}

fun FieldsHolder.byte(description: String, index: Int): ByteField {
    return ByteField(description, index).also {
        this.addField(it)
    }
}

fun FieldsHolder.word(description: String, index: Int): WordField {
    return WordField(description, index).also {
        this.addField(it)
    }
}

fun FieldsHolder.dword(description: String, index: Int): DWordField {
    return DWordField(description, index).also {
        this.addField(it)
    }
}

fun FieldsHolder.string(
    description: String,
    index: Int,
    lengthField: FieldDefined,
    charset: Charset
): StringField {
    return StringField(description, index, lengthField, charset).also {
        this.addField(it)
    }
}
