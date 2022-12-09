package ru.javersingleton.bdui.engine.function

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

interface Function : ByTypeStrategy.Element<String> {

    fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData>

}