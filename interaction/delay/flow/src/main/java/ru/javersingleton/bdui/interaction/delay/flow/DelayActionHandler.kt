package ru.javersingleton.bdui.interaction.delay.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.handler.flow.BaseActionHandler
import java.util.concurrent.TimeUnit

object DelayActionHandler : BaseActionHandler<DelayAction>(DelayAction.Factory) {

    override fun handleAction(
        currentState: ComponentField,
        action: DelayAction
    ): Flow<Interaction> = flow {
        delay(TimeUnit.SECONDS.toMillis(action.seconds.toLong()))
        emit(action.onFinished())
    }

    override fun checkRelevant(key: Interaction): Boolean =
        key is DelayAction

}