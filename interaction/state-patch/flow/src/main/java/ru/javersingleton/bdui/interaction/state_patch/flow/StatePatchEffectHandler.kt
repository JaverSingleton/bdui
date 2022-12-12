package ru.javersingleton.bdui.interaction.state_patch.flow

import ru.javersingleton.bdui.engine.field.entity.ComponentField
import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.plus
import ru.javersingleton.bdui.handler.flow.BaseEffectHandler
import ru.javersingleton.bdui.interaction.base.effect.StatePatchEffect

object StatePatchEffectHandler : BaseEffectHandler<StatePatchEffect>(StatePatchEffect.Factory) {

    override fun handleEffect(
        currentState: ComponentField,
        effect: StatePatchEffect
    ): ComponentField = currentState + effect.patch

    override fun checkRelevant(key: Interaction): Boolean =
        key is StatePatchEffect

}