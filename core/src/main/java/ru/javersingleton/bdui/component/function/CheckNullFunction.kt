package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.PrimitiveData
import ru.javersingleton.bdui.core.field.ResolvedData
import ru.javersingleton.bdui.core.field.StructureData

class CheckNullFunction: ru.javersingleton.bdui.core.field.Function {
    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value = params.prop("value").current
            PrimitiveData(
                id = "$id@result",
                (value == null).toString()
            )
        }
    }
}