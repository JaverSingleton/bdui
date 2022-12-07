package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.PrimitiveData
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData
import ru.javersingleton.bdui.engine.function.Function


object ConditionFunction: Function {

    override val key: String = "Condition"


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