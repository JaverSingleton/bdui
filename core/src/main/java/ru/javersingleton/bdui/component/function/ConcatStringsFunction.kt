package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ArrayData
import ru.javersingleton.bdui.core.field.Primitive
import ru.javersingleton.bdui.core.field.Structure

class ConcatStringsFunction : ru.javersingleton.bdui.core.field.Function {
    override fun calculate(scope: Lambda.Scope, id: String, params: Structure): Value<Any?> =
        scope.run {
            rememberValue(id, params) {
                val values: ArrayData = params.prop("strings").current()
                val result = values.fields.joinToString(separator = "") {
                    it.value.current<Primitive>().toString()
                }
                Primitive(result)
            }
        }
}