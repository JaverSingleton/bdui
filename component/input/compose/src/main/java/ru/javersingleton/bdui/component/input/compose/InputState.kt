package ru.javersingleton.bdui.component.input.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.core.ConstValue
import ru.javersingleton.bdui.engine.field.PrimitiveData
import ru.javersingleton.bdui.engine.field.StructureData

object InputStateFactory : ComponentStateFactory<InputState>() {

    override val type: String = "Input"

    override fun Scope.create(componentType: String): InputState =
        InputState(
            text = prop("text").asString() ?: "",
            onTextChanged = prop("onTextChanged").asInteraction()?.let { callback ->
                {
                    callback(
                        mapOf(
                            "args" to ConstValue(
                                StructureData(
                                    fields = mapOf(
                                        "text" to ConstValue(
                                            PrimitiveData(value = it)
                                        )
                                    )
                                )
                            )
                        )
                    )
                }
            }
        )

}

data class InputState(
    val text: String,
    val onTextChanged: ((String) -> Unit)?,
)