package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.CalculableValue
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.field.newId
import ru.javersingleton.bdui.engine.function.Function


object RandomStringFunction: Function {

    override val type: String = "RandomString"

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> = scope.run {
        CalculableValue {
            PrimitiveData(value = newId())
        }
    }

}