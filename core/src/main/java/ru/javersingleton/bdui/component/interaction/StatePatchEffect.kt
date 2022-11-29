package ru.javersingleton.bdui.component.interaction

import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.currentQuiet
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.EmptyField
import ru.javersingleton.bdui.core.field.Field
import ru.javersingleton.bdui.core.field.ResolvedData
import ru.javersingleton.bdui.core.interaction.Effect
import ru.javersingleton.bdui.core.plus

data class StatePatchEffect(
    val patch: Field<*>
): Effect {

    override fun run(state: ComponentField): ComponentField = state + patch

    class Factory: Effect.Factory {

        override fun create(params: Map<String, Value<*>>): Effect {
            val data: ResolvedData? = params["patch"]?.currentQuiet()
            return StatePatchEffect(
                patch = data?.asField() ?: EmptyField()
            )
        }

    }

}