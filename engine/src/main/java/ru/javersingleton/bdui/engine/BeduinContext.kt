package ru.javersingleton.bdui.engine

import ru.javersingleton.bdui.engine.register.ComponentStatesRegister
import ru.javersingleton.bdui.engine.register.EffectsRegister
import ru.javersingleton.bdui.engine.register.FunctionsRegister
import ru.javersingleton.bdui.engine.function.Function
import ru.javersingleton.bdui.engine.interaction.Effect
import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.meta.MetaComponentBlueprint
import ru.javersingleton.bdui.engine.meta.MetaComponentsStorage

interface BeduinContext {

    fun inflateFunction(functionType: String): Function

    fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint

    fun inflateStateFactory(componentType: String): ComponentStateFactory<*>

    fun sendInteraction(interaction: Interaction)

    fun inflateInteractionFactory(
        type: String,
        name: String
    ): Interaction.Factory

}

class MainBeduinContext(
    private val metaComponents: MetaComponentsStorage,
    private val nativeComponents: ComponentStatesRegister,
    private val functions: FunctionsRegister,
    private val effects: EffectsRegister,
) : BeduinContext {

    override fun inflateFunction(functionType: String): Function =
        functions[functionType]

    override fun inflateMetaComponentBlueprint(componentType: String): MetaComponentBlueprint =
        metaComponents[componentType]
            ?: throw IllegalArgumentException("MetaComponent $componentType not found")

    override fun inflateStateFactory(componentType: String): ComponentStateFactory<*> =
        nativeComponents[componentType]

    // TODO Убрать метод используя везде BeduinControllerContext
    override fun sendInteraction(interaction: Interaction) {
        // Do Nothing
    }

    override fun inflateInteractionFactory(type: String, name: String): Interaction.Factory =
        when (type) {
            Effect.TYPE -> inflateEffectFactory(name)
            else -> throw IllegalArgumentException("Interaction with type $type not found")
        }

    private fun inflateEffectFactory(name: String): Interaction.Factory =
        effects[name]

}