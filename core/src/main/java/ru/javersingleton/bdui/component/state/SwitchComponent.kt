package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.ConstValue
import ru.javersingleton.bdui.core.field.PrimitiveData
import ru.javersingleton.bdui.core.field.StructureData

object SwitchComponent {

    object StateFactory : ComponentState.Factory<SwitchState>() {

        override fun Scope.create(componentType: String): SwitchState = SwitchState(
            checked = prop("checked").asBoolean() ?: false,
            enabled = prop("enabled").asBoolean() ?: true,
            onCheckedChange = prop("onCheckedChange").asInteraction()?.let { callback ->
                {
                    callback(
                        mapOf(
                            "args" to ConstValue(
                                StructureData(
                                    fields = mapOf(
                                        "checked" to ConstValue(
                                            PrimitiveData(value = it.toString())
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

data class SwitchState(
    val checked: Boolean,
    val enabled: Boolean,
    val onCheckedChange: ((Boolean) -> Unit)?,
)