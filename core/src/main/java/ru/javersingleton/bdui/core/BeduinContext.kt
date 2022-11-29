package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.component.function.*
import ru.javersingleton.bdui.component.interaction.StatePatchEffect
import ru.javersingleton.bdui.component.state.*
import ru.javersingleton.bdui.core.function.Function
import ru.javersingleton.bdui.core.interaction.Interaction

interface BeduinContext {

    fun inflateFunction(functionType: String): Function

    fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint

    fun inflateStateFactory(componentType: String): ComponentState.Factory<*>

    fun sendInteraction(interaction: Interaction)

    fun inflateInteractionFactory(
        type: String,
        name: String
    ): Interaction.Factory

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
            "MaxLength" -> MaxLengthFunction()
            "Not" -> NotFunction()
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
            "Input" -> InputComponent.StateFactory
            "Switch" -> SwitchComponent.StateFactory
            "Image" -> ImageComponent.StateFactory
            "Toolbar" -> ToolbarComponent.StateFactory
            else -> MetaComponent.StateFactory
        }

    override fun sendInteraction(interaction: Interaction) {
        // Do Nothing
    }

    override fun inflateInteractionFactory(type: String, name: String): Interaction.Factory =
        when (type) {
            "effect" -> inflateEffectFactory(name)
            else -> throw IllegalArgumentException("Interaction with type $type not found")
        }

    private fun inflateEffectFactory(name: String): Interaction.Factory = when (name) {
        "StatePatch" -> StatePatchEffect.Factory()
        else -> throw IllegalArgumentException("Effect $name not found")
    }

}