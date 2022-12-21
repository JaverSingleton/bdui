package ru.javersingleton.bdui.engine.function

import ru.javersingleton.bdui.engine.core.Scope
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

interface Function : ByTypeStrategy.Element<String> {

    fun calculate(scope: Scope, id: String, params: StructureData): Value<ResolvedData>

}