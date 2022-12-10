package ru.javersingleton.bdui.component.switch.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.core.ConstValue
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.field.entity.StructureData

object SwitchStateFactory : ComponentStateFactory<SwitchState>() {

    override val type: String = "Switch"

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

data class SwitchState(
    val checked: Boolean,
    val enabled: Boolean,
    val onCheckedChange: ((Boolean) -> Unit)?,
)