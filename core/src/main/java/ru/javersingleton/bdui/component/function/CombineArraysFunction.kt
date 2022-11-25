package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ArrayData
import ru.javersingleton.bdui.core.field.Structure

class CombineArraysFunction : ru.javersingleton.bdui.core.field.Function {
    override fun calculate(scope: Lambda.Scope, id: String, params: Structure): Value<Any?> =
        scope.run {
            rememberValue(id, params) {
                val values: ArrayData = params.prop("arrays").current()
                ArrayData(
                    values.fields.flatMap {
                        val arrayData: ArrayData = it.value.current()
                        arrayData.fields
                    }
                )
            }
        }
}