package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class TransformField(
    override val id: String,
    private val params: Field<Structure>,
    private val transformType: String,
) : Field<Any?> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<Any?> = scope.run {
        val params = params.resolve(this, args)
        if (params !is ResolvedField) {
            return TransformField(
                id,
                params,
                transformType,
            )
        }

        val transform = inflateTransform(transformType)

        ResolvedField(
            id = id,
            value = rememberValue(
                id,
                setOf(this@TransformField.params, transform)
            ) { transform.calculate(params.value.current) }
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<Any?> {
        return if (targetFieldId != id) {
            val targetParams = params.mergeDeeply(targetFieldId, targetField)
            if (targetParams != params) {
                this.copy(params = targetParams)
            } else {
                this
            }
        } else {
            if (targetField is TransformField) {
                params.mergeDeeply(params.id, targetField.params)
            } else {
                targetField
            } as Field<Any?>
        }
    }


}

interface Transform {

    // TODO Переписать Transform
    fun calculate(params: Any?): Any?

}