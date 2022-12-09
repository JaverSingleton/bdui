package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.PrimitiveData
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData
import ru.javersingleton.bdui.engine.function.Function

object MaxLengthFunction: Function {

    override val type: String = "MaxLength"

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value: PrimitiveData = params.prop("value").current { PrimitiveData(value = "") }
            val length: PrimitiveData = params.prop("length").current { PrimitiveData(value = "0") }
            PrimitiveData(
                value = value.asString().take(length.asInt())
            )
        }
    }
}