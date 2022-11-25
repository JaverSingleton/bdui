package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.function.Function

class CheckEqualsFunction: Function {
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