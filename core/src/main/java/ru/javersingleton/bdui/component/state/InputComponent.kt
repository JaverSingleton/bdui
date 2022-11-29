package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.ConstValue
import ru.javersingleton.bdui.core.field.PrimitiveData
import ru.javersingleton.bdui.core.field.StructureData

object InputComponent {

    object StateFactory : ComponentState.Factory<InputState>() {

        override fun Scope.create(componentType: String): InputState = InputState(
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

}

data class InputState(
    val text: String,
    val onTextChanged: ((String) -> Unit)?,
)