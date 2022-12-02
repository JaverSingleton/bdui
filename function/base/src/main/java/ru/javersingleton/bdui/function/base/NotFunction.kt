package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.*
import ru.javersingleton.bdui.engine.function.Function


object NotFunction: Function {

    override val type: String = "Not"

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value: PrimitiveData = params.prop("value").current()
                ?: throw IllegalArgumentException("You must set value for Not")
            if (value.asBoolean()) {
                PrimitiveData(id = "$id@value", "false")
            } else {
                PrimitiveData(id = "$id@value", "true")
            }
        }
    }

}