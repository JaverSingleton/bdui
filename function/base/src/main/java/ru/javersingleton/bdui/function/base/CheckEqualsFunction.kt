package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Scope
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.function.Function

object CheckEqualsFunction: Function {

    override val type: String = "CheckEquals"

    override fun calculate(scope: Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value1 = params.prop("value1").current
            val value2 = params.prop("value2").current
            PrimitiveData(
                value = (value1 == value2).toString()
            )
        }
    }
}