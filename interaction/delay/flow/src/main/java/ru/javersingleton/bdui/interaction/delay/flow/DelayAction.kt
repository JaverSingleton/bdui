package ru.javersingleton.bdui.interaction.delay.flow

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.InteractionData
import ru.javersingleton.bdui.engine.field.PrimitiveData
import ru.javersingleton.bdui.engine.interaction.Interaction

data class DelayAction(
    val seconds: Int,
    val onFinished: () -> Interaction
): Interaction {

    object Factory: Interaction.Factory {

        override val type: String = "Delay"

        override fun create(params: Map<String, Value<*>>): Interaction {
            val seconds = params["seconds"]?.currentQuiet<PrimitiveData>()?.asInt() ?: 0
            val onFinished = params["onFinished"]?.currentQuiet<InteractionData>()
                ?: throw IllegalArgumentException("You must set onFinished for DelayAction")
            return DelayAction(
                seconds = seconds,
                onFinished = {
                    onFinished(mapOf())
                }
            )
        }
    }

}