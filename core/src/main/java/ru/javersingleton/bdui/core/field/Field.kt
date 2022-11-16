package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

interface Field {

    fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field?

    val id: String

}

data class ResolvedField(
    override val id: String,
    val state: State<*>,
) : Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field = this

}