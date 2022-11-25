package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.component.function.CheckEqualsFunction
import ru.javersingleton.bdui.component.function.CheckNullFunction
import ru.javersingleton.bdui.component.function.CombineArraysFunction
import ru.javersingleton.bdui.component.function.ConditionFunction
import ru.javersingleton.bdui.component.state.*
import ru.javersingleton.bdui.core.field.Function
import ru.javersingleton.bdui.core.interaction.Interaction

interface BeduinContext {

    fun inflateFunction(functionType: String): Function

    fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint

    fun inflateStateFactory(componentType: String): ComponentState.Factory<*>

    fun sendInteraction(interaction: Interaction)

    fun inflateInteraction(
        type: String,
        name: String
    ): Interaction

}

class MainBeduinContext(
    private val metaComponents: ComponentsCache
) : BeduinContext {

    override fun inflateFunction(functionType: String): Function =
        when (functionType) {
            "Condition" -> ConditionFunction()
            "CheckEquals" -> CheckEqualsFunction()
            "CombineArrays" -> CombineArraysFunction()
            "CheckNull" -> CheckNullFunction()
            else -> throw IllegalArgumentException("Function $functionType not found")
        }

    override fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint =
        metaComponents.get(componentType)
            ?: throw IllegalArgumentException("Component $componentType not found")

    override fun inflateStateFactory(componentType: String): ComponentState.Factory<*> =
        when (componentType) {
            "Box" -> BoxComponent.StateFactory
            "Column" -> ColumnComponent.StateFactory
            "Row" -> RowComponent.StateFactory
            "Text" -> TextComponent.StateFactory
            "Button" -> ButtonComponent.StateFactory
            "Image" -> ImageComponent.StateFactory
            "Toolbar" -> ToolbarComponent.StateFactory
            else -> MetaComponent.StateFactory
        }

    override fun sendInteraction(interaction: Interaction) {
        TODO("Not yet implemented")
    }

    override fun inflateInteraction(type: String, name: String): Interaction {
        TODO("Not yet implemented")
    }

}