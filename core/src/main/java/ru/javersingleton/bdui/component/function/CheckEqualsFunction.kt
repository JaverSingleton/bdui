package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.Primitive
import ru.javersingleton.bdui.core.field.Structure

class CheckEqualsFunction: ru.javersingleton.bdui.core.field.Function {
    override fun calculate(scope: Lambda.Scope, id: String, params: Structure): Value<Any?> = scope.run {
        rememberValue(id, params) {
            val value1 = params.prop("value1").current
            val value2 = params.prop("value2").current
            Primitive((value1 == value2).toString())
        }
    }
}