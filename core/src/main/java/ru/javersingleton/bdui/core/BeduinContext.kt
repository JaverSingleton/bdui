package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.component.state.*
import ru.javersingleton.bdui.core.field.*

interface BeduinContext {

    fun inflateTransform(transformType: String): Transform

    fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint

    fun inflateStateFactory(componentType: String): ComponentState.Factory<*>

}

class MainBeduinContext() : BeduinContext {

    private val metaComponents = mapOf(
        "ListItem" to {
            MetaComponentBlueprint(
                state = StructureField(
                    "title" to PrimitiveField(""),
                    "subtitle" to PrimitiveField(""),
                    "hint" to ReferenceField("subtitle"),
                    "footer" to EmptyField(),
                ),
                rootComponent = ComponentField(
                    type = "Column",
                    "children" to ArrayField(
                        ComponentField(
                            type = "Text",
                            "text" to ReferenceField("title"),
                            "textSize" to PrimitiveField("20"),
                            "layout_width" to PrimitiveField("fillMaxWidth"),
                        ),
                        ComponentField(
                            type = "Text",
                            "text" to ReferenceField("subtitle"),
                            "textSize" to PrimitiveField("12"),
                            "layout_width" to PrimitiveField("fillMaxWidth"),
                        ),
                        ComponentField(
                            type = "Text",
                            "text" to ReferenceField("hint"),
                            "textSize" to PrimitiveField("8"),
                            "layout_width" to PrimitiveField("fillMaxWidth"),
                        ),
                        ReferenceField("footer")
                    )
                )
            )
        }
    )

    override fun inflateTransform(transformType: String): Transform {
        TODO("Not yet implemented")
    }

    override fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint =
        when {
            metaComponents.containsKey(componentType) -> metaComponents[componentType]!!()
            else -> throw IllegalArgumentException()
        }

    override fun inflateStateFactory(componentType: String): ComponentState.Factory<*> =
        when (componentType) {
            "Box" -> BoxComponent.StateFactory
            "Column" -> ColumnComponent.StateFactory
            "Text" -> TextComponent.StateFactory
            "Button" -> ButtonComponent.StateFactory
            "Image" -> ImageComponent.StateFactory
            else -> MetaComponent.StateFactory
        }

}