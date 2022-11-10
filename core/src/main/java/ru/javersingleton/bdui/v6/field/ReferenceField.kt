package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.Lambda


class ReferenceField(
    private val refFieldName: String,
    override val id: String
) : Field {

    override fun resolve(scope: Lambda.Scope): Field = scope.run {
        ResolvedField(
            id = id,
            state = argument(refFieldName),
        )
    }

}