package ru.javersingleton.bdui.interaction.delay.flow

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
import ru.javersingleton.bdui.engine.interaction.Interaction

data class ValueCallbackEvent(
    val value: PrimitiveData
): Interaction {

    object Factory: Interaction.Factory {

        override val type: String = "ValueCallback"

        override fun create(params: Map<String, Value<*>>): Interaction {
            val value = params["value"]?.currentQuiet<PrimitiveData>()
                ?: throw IllegalArgumentException("You must set value for ValueCallback")
            return ValueCallbackEvent(
                value = value
            )
        }
    }

}