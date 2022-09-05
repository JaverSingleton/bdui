package ru.javersingleton.bdui.v3

import ru.javersingleton.bdui.v3.core.field.FieldsHolder

data class TransformField(
    private val transform: Transform,
    private val params: Field?,
) : Field {

    override fun resolve(source: FieldsHolder): Field? {
        val params = params?.resolve(source)
        if (params !is ResolvedField?) {
            return TransformField(
                transform = transform,
                params = params
            )
        }
        return transform.resolve(params)
    }

}

interface Transform {

    fun resolve(params: ResolvedField?): Field?

}