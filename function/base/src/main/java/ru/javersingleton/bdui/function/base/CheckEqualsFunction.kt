package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.*
import ru.javersingleton.bdui.engine.function.Function

object CheckEqualsFunction: Function {

    override val type: String = "CheckEquals"

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value1 = params.prop("value1").current
            val value2 = params.prop("value2").current
            PrimitiveData(
                id = "$id@result",
                (value1 == value2).toString()
            )
        }
    }
}