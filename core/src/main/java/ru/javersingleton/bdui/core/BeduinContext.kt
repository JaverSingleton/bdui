package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.component.function.CheckEqualsFunction
import ru.javersingleton.bdui.component.function.CombineArraysFunction
import ru.javersingleton.bdui.component.function.ConcatStringsFunction
import ru.javersingleton.bdui.component.function.ConditionFunction
import ru.javersingleton.bdui.component.state.*
import ru.javersingleton.bdui.core.field.Function

interface BeduinContext {

    fun inflateFunction(functionType: String): Function

    fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint

    fun inflateStateFactory(componentType: String): ComponentState.Factory<*>

}

class MainBeduinContext(
    private val metaComponents: ComponentsCache
) : BeduinContext {

    override fun inflateFunction(functionType: String): Function =
        when (functionType) {
            "Condition" -> ConditionFunction()
            "CheckEquals" -> CheckEqualsFunction()
            "CombineArrays" -> CombineArraysFunction()
            "ConcatStrings" -> ConcatStringsFunction()
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

}