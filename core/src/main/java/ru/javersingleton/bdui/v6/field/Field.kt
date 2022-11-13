package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.Lambda
import ru.javersingleton.bdui.v6.State

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