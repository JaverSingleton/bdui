package ru.javersingleton.bdui.interaction.base.effect

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.EmptyField
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.interaction.Interaction

data class StatePatchEffect(
    val patch: Field<*>
): Interaction {

    object Factory: Interaction.Factory {

        override val key: String = "StatePatch"

        override fun create(params: Map<String, Value<*>>): Interaction {
            val data: ResolvedData? = params["patch"]?.currentQuiet()
            return StatePatchEffect(
                patch = data?.asField() ?: EmptyField()
            )
        }
    }

}