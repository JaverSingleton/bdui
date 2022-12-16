package ru.javersingleton.bdui.interaction.base.effect

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.entity.EmptyField
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.interaction.Interaction

data class StatePatchEffect(
    val targetId: String?,
    val patch: Field<*>
): Interaction {

    object Factory: Interaction.Factory {

        override val type: String = "StatePatch"

        override fun create(params: Map<String, Value<*>>): Interaction {
            val data: ResolvedData? = params["patch"]?.currentQuiet()
            val targetId: PrimitiveData? = params["targetId"]?.currentQuiet()
            return StatePatchEffect(
                targetId = targetId?.asString(),
                patch = data?.asField() ?: EmptyField()
            )
        }
    }

}