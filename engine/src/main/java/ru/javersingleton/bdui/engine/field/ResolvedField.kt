package ru.javersingleton.bdui.engine.field

import ru.javersingleton.bdui.engine.ArgumentsStorage
import ru.javersingleton.bdui.engine.core.Scope
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

    override fun resolve(scope: Scope, args: ArgumentsStorage): Field<T> = this

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T> = this

    override fun copyWithId(id: String): Field<T> = copy(id = id)

    override fun toString(): String {
        return "ResolvedField(id=$id, value=$value)"
    }
}