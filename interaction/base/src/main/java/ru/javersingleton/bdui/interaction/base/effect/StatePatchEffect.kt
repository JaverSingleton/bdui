package ru.javersingleton.bdui.interaction.base.effect

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.field.EmptyField
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.interaction.Effect
import ru.javersingleton.bdui.engine.plus

data class StatePatchEffect(
    val patch: Field<*>
): Effect {

    override fun run(state: ComponentField): ComponentField = state + patch

    object Factory: Effect.Factory {

        override val type: String = "StatePatch"

        override fun create(params: Map<String, Value<*>>): Effect {
            val data: ResolvedData? = params["patch"]?.currentQuiet()
            return StatePatchEffect(
                patch = data?.asField() ?: EmptyField()
            )
        }
    }

}