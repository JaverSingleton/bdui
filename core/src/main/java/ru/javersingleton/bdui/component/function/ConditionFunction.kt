package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.Function
import ru.javersingleton.bdui.core.field.Primitive
import ru.javersingleton.bdui.core.field.Structure


class ConditionFunction: Function {

    override fun calculate(scope: Lambda.Scope, id: String, params: Structure): Value<Any?> = scope.run {
        rememberValue(id, params) {
            if (params.prop("value").current<Primitive>().toBoolean()) {
                params.prop("trueResult")
            } else {
                params.prop("falseResult")
            }.current
        }
    }

}