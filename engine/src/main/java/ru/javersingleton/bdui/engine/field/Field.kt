package ru.javersingleton.bdui.engine.field

import ru.javersingleton.bdui.engine.ArgumentsStorage
import ru.javersingleton.bdui.engine.core.Scope
import java.util.*

interface Field<T : ResolvedData> {

    fun resolve(scope: Scope, args: ArgumentsStorage): Field<T>

    fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T>

    fun copyWithId(id: String): Field<T>

    val id: String

    val withUserId: Boolean

}

interface ResolvedData {
    fun asField(): Field<*>
}

fun newId(): String = UUID.randomUUID().toString()