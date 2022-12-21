package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Scope
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.function.Function

object MaxLengthFunction: Function {

    override val type: String = "MaxLength"

    override fun calculate(scope: Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value: PrimitiveData = params.prop("value").current { PrimitiveData(value = "") }
            val length: PrimitiveData = params.prop("length").current { PrimitiveData(value = "0") }
            PrimitiveData(
                value = value.asString().take(length.asInt())
            )
        }
    }
}