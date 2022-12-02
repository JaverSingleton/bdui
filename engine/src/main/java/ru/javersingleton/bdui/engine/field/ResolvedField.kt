package ru.javersingleton.bdui.engine.field

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.References
import ru.javersingleton.bdui.engine.core.Value

data class ResolvedField<T: ResolvedData>(
    override val id: String,
    override val withUserId: Boolean,
    val value: Value<T>,
    val dataWithUserId: Map<String, Value<*>>
) : Field<T> {

    constructor(value: Value<T>): this(
        id = newId(),
        withUserId = false,
        value = value,
        dataWithUserId = mapOf(),
    )

    override fun resolve(scope: Lambda.Scope, args: References): Field<T> = this

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T> = this

    override fun copyWithId(id: String): Field<T> = copy(id = id)

}