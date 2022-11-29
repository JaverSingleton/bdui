package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import java.util.*

interface Field<T: ResolvedData> {

    fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<T>

    fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T>

    fun copyWithId(id: String): Field<T>

    val id: String

    val withUserId: Boolean

}

data class ResolvedField<T: ResolvedData>(
    override val id: String,
    override val withUserId: Boolean,
    val value: Value<T>,
) : Field<T> {

    constructor(
        id: String,
        value: Value<T>
    ) : this(id = id, withUserId = true, value)

    constructor(
        value: Value<T>
    ) : this(id = newId(), withUserId = false, value)

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<T> = this

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T> = this

    override fun copyWithId(id: String): Field<T> = copy(id = id)

}

interface ResolvedData {
    fun toField(): Field<*>
}

fun newId(): String = UUID.randomUUID().toString()