package ru.javersingleton.bdui.interaction.delay.flow

import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.handler.flow.BaseEventHandler

object ValueCallbackEventHandler :
    BaseEventHandler<ValueCallbackEvent>(ValueCallbackEvent.Factory) {

    override fun handleEvent(
        currentState: ComponentField,
        event: ValueCallbackEvent
    ) {
        // Do Nothing
    }

    override fun checkRelevant(key: Interaction): Boolean =
        key is ValueCallbackEvent

}