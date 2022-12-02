package ru.javersingleton.bdui.engine.function

import ru.javersingleton.bdui.engine.register.Register
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData

interface Function: Register.Element {

    fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData>

}