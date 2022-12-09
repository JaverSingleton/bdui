package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.PrimitiveData
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData
import ru.javersingleton.bdui.engine.function.Function

object CheckNullFunction: Function {

    override val type: String = "CheckNull"

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value = params.prop("value").current
            PrimitiveData(
                value = (value == null).toString()
            )
        }
    }
}