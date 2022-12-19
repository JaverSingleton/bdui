package ru.javersingleton.bdui.render.android_view

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.core.LazyValue
import ru.javersingleton.bdui.engine.core.SimpleScope
import ru.javersingleton.bdui.engine.field.entity.ComponentData
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

abstract class ComponentFactory<T>(
    val stateFactory: ComponentStateFactory<T>
) : ByTypeStrategy.Element<String> {

    abstract fun createComponent(context: BeduinViewContext): Component

    fun createEmptyState(context: BeduinViewContext): ComponentData {
        val params = StructureData()
        return ComponentData(
            componentType = type,
            params = params,
            value = LazyValue {
                stateFactory.calculate(
                    componentType = type,
                    scope = SimpleScope(context),
                    args = params
                )
            }
        )
    }

}