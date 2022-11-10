package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.ConstState
import ru.javersingleton.bdui.v6.Lambda

class PrimitiveField(
    override val id: String,
    private val value: String,
) : Field {

    override fun resolve(scope: Lambda.Scope): Field = scope.run {
        ResolvedField(
            id = id,
            state = ConstState(value),
        )
    }

}