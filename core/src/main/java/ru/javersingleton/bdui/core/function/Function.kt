package ru.javersingleton.bdui.core.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ResolvedData
import ru.javersingleton.bdui.core.field.StructureData

interface Function {

    fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData>

}