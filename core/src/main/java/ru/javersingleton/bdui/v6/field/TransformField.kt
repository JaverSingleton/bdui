package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.Lambda
import ru.javersingleton.bdui.v6.State

data class TransformField(
    override val id: String,
    private val params: Field?,
    private val transformType: String,
) : Field {

    override fun resolve(scope: Lambda.Scope): Field? = scope.run {
        val params = params?.resolve(this)
        if (params !is ResolvedField?) {
            return TransformField(
                id,
                params,
                transformType,
            )
        }

        val transform = inflateTransform(transformType)
        ResolvedField(
            id = id,
            state = transform.calculate(params?.state)
        )
    }


}

interface Transform {

    fun calculate(params: State?): State

}