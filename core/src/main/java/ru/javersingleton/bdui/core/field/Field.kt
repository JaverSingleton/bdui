package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.References
import ru.javersingleton.bdui.core.Value
import java.util.*

interface Field<T : ResolvedData> {

    fun resolve(scope: Lambda.Scope, args: References): Field<T>

    fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T>

    fun copyWithId(id: String): Field<T>

    val id: String

    val withUserId: Boolean

}

interface ResolvedData {
    fun asField(): Field<*>
}

fun newId(): String = UUID.randomUUID().toString()