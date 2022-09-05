package ru.javersingleton.bdui.v3.core.field

import ru.javersingleton.bdui.v3.Field
import ru.javersingleton.bdui.v3.ResolvedField


data class ValueField(private val value: String) : Field, ResolvedField {

    override fun resolve(source: FieldsHolder): Field = this

    fun getStringValue(): String = value

    fun getIntValue(): Int = value.toInt()

    fun getBooleanValue(): Boolean = value.toBoolean()

}

fun Field.toStringValue(): String = (this as ValueField).getStringValue()
fun Field.toIntValue(): Int = (this as ValueField).getIntValue()
fun Field.toBooleanValue(): Boolean = (this as ValueField).getBooleanValue()
fun Field.toStructureField(): StructureField = (this as StructureField)