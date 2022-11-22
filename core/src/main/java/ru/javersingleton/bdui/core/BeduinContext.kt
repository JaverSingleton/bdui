package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.component.function.CheckEqualsFunction
import ru.javersingleton.bdui.component.function.ConditionFunction
import ru.javersingleton.bdui.component.state.*
import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.field.Function

interface BeduinContext {

    fun inflateFunction(functionType: String): Function

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
                            "layout_width" to PrimitiveField("fillMax"),
                        ),
                        ComponentField(
                            type = "Text",
                            "text" to ReferenceField("subtitle"),
                            "textSize" to PrimitiveField("12"),
                            "layout_width" to PrimitiveField("fillMax"),
                        ),
                        ComponentField(
                            type = "Text",
                            "text" to ReferenceField("hint"),
                            "textSize" to PrimitiveField("8"),
                            "layout_width" to PrimitiveField("fillMax"),
                        ),
                        ReferenceField("footer")
                    )
                )
            )
        },
        "ContactItem" to {
            MetaComponentBlueprint(
                state = StructureField(
                    "avatar" to PrimitiveField(""),
                    "name" to PrimitiveField(""),
                    "lastSeen" to PrimitiveField(""),
                    "indicator" to PrimitiveField("false"),
                ),
                rootComponent = ComponentField(
                    type = "Box",
                    "children" to ArrayField(
                        ComponentField(
                            type = "Row",
                            "layout_width" to PrimitiveField("fillMax"),
                            "layout_padding" to StructureField(
                                "start" to PrimitiveField("16"),
                                "end" to PrimitiveField("16"),
                                "top" to PrimitiveField("8"),
                                "bottom" to PrimitiveField("8"),
                            ),
                            "children" to ArrayField(
                                ComponentField(
                                    type = "Box",
                                    "layout_width" to PrimitiveField("72"),
                                    "layout_height" to PrimitiveField("72"),
                                    "children" to ArrayField(
                                        ComponentField(
                                            type = "Image",
                                            "src" to ReferenceField("avatar"),
                                            "contentScale" to PrimitiveField("Crop"),
                                            "clip" to PrimitiveField("Circle"),
                                            "layout_width" to PrimitiveField("fillMax"),
                                            "layout_height" to PrimitiveField("fillMax"),
                                        ),
                                        ComponentField(
                                            type = "Image",
                                            "src" to FunctionField(
                                                type = "Condition",
                                                "value" to ReferenceField("indicator"),
                                                "trueResult" to PrimitiveField("https://zibuhoker.ru/ifm/indicator.png"),
                                                "falseResult" to PrimitiveField(""),
                                            ),
                                            "layout_alignment" to PrimitiveField("BottomEnd"),
                                            "contentScale" to PrimitiveField("Inside"),
                                            "clip" to PrimitiveField("Circle"),
                                            "layout_width" to PrimitiveField("24"),
                                            "layout_height" to PrimitiveField("24"),
                                            "layout_padding" to StructureField(
                                                "end" to PrimitiveField("2"),
                                                "bottom" to PrimitiveField("2")
                                            )
                                        ),
                                    ),
                                ),
                                ComponentField(
                                    type = "Column",
                                    "layout_width" to PrimitiveField("fillMax"),
                                    "children" to ArrayField(
                                        ComponentField(
                                            type = "Text",
                                            "text" to ReferenceField("name"),
                                            "textSize" to PrimitiveField("16"),
                                            "layout_width" to PrimitiveField("fillMax"),
                                            "layout_padding" to StructureField(
                                                "top" to PrimitiveField("8"),
                                                "start" to PrimitiveField("8")
                                            ),
                                        ),
                                        ComponentField(
                                            type = "Text",
                                            "text" to FunctionField(
                                                type = "Condition",
                                                "value" to ReferenceField("indicator"),
                                                "trueResult" to PrimitiveField("Online"),
                                                "falseResult" to ReferenceField("lastSeen"),
                                            ),
                                            "textSize" to PrimitiveField("12"),
                                            "layout_width" to PrimitiveField("fillMax"),
                                            "layout_padding" to StructureField(
                                                "top" to PrimitiveField("8"),
                                                "start" to PrimitiveField("8"),
                                                "bottom" to PrimitiveField("8")
                                            ),
                                        ),
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
    )

    override fun inflateFunction(functionType: String): Function =
        when (functionType) {
            "Condition" -> ConditionFunction()
            "CheckEquals" -> CheckEqualsFunction()
            else -> throw IllegalArgumentException("Function $functionType not found")
        }

    override fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint =
        when {
            metaComponents.containsKey(componentType) -> metaComponents[componentType]!!()
            else -> throw IllegalArgumentException("Component $componentType not found")
        }

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