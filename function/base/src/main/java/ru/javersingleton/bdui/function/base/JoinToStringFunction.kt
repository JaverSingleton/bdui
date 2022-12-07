package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ArrayData
import ru.javersingleton.bdui.engine.field.PrimitiveData
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData
import ru.javersingleton.bdui.engine.function.Function

object JoinToStringFunction : Function {

    override val key: String = "JoinToString"


    override fun calculate(
        scope: Lambda.Scope,
        id: String,
        params: StructureData
    ): Value<ResolvedData> =
        scope.run {
            rememberValue(id, params) {
                val values: ArrayData = params.prop("items")
                    .current()
                    ?: return@rememberValue PrimitiveData(value = "")
                val separator: String = params.prop("separator")
                    .current<PrimitiveData>()
                    ?.asString()
                    ?: ""
                val result = values.fields.joinToString(separator = separator) {
                    it.current<PrimitiveData>().toString()
                }
                PrimitiveData(value = result)
            }
        }

}