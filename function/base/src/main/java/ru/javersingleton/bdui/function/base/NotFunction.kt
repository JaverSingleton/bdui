package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Scope
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.function.Function


object NotFunction: Function {

    override val type: String = "Not"

    override fun calculate(scope: Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        rememberValue(id, params) {
            val value: PrimitiveData = params.prop("value").current()
                ?: throw IllegalArgumentException("You must set value for Not")
            if (value.asBoolean()) {
                PrimitiveData(value = "false")
            } else {
                PrimitiveData(value = "true")
            }
        }
    }

}