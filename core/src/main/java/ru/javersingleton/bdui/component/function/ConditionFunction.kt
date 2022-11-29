package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.function.Function


class ConditionFunction: Function {

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value: PrimitiveData = params.prop("value").current()
                ?: throw IllegalArgumentException("You must set value for ConditionFunction")
            if (value.asBoolean()) {
                params.prop("trueResult")
            } else {
                params.prop("falseResult")
            }.current { it }
        }
    }

}