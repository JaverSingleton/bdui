package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

data class TransformField(
    override val id: String,
    private val params: Field?,
    private val transformType: String,
) : Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field = scope.run {
        val params = params?.resolve(this, args)
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
            state = rememberState(
                id,
                setOf(this@TransformField.params, transform)
            ) { transform.calculate(params?.state?.value) }
        )
    }


}

interface Transform {

    // TODO Переписать Transform
    fun calculate(params: Any?): Any?

}