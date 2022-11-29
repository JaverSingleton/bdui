package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.PrimitiveData
import ru.javersingleton.bdui.core.field.ResolvedData
import ru.javersingleton.bdui.core.field.StructureData
import ru.javersingleton.bdui.core.function.Function

class MaxLengthFunction: Function {
    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value: PrimitiveData = params.prop("value").current { PrimitiveData(value = "") }
            val length: PrimitiveData = params.prop("length").current { PrimitiveData(value = "0") }
            PrimitiveData(
                id = "$id@result",
                value.asString().take(length.asInt())
            )
        }
    }
}