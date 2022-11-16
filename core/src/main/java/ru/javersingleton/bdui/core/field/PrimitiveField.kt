package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

data class PrimitiveField(
    override val id: String,
    private val value: String,
) : Field {

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, State<*>>
    ): Field = scope.run {
        ResolvedField(
            id = id,
            state = rememberState(id, value) { Primitive(value) },
        )
    }

}

data class Primitive(private val value: String) {

    override fun toString(): String = value

    fun toInt(): Int = value.toInt()

    fun toFloat(): Float = value.toFloat()

}