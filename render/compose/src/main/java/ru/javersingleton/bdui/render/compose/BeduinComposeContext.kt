package ru.javersingleton.bdui.render.compose

import ru.javersingleton.bdui.engine.BeduinContext
import ru.javersingleton.bdui.engine.MainBeduinContext
import ru.javersingleton.bdui.engine.meta.MetaComponentsStorage
import ru.javersingleton.bdui.engine.register.EffectsRegister
import ru.javersingleton.bdui.engine.register.FunctionsRegister

class BeduinComposeContext(
    beduinContext: BeduinContext,
    private val componentsRegister: ComponentsRegister
) : BeduinContext by beduinContext {

    constructor(
        metaComponents: MetaComponentsStorage,
        nativeComponents: ComponentsRegister,
        functions: FunctionsRegister,
        effects: EffectsRegister,
    ) : this(
        beduinContext = MainBeduinContext(
            metaComponents,
            nativeComponents.createComponentStatesRegister(),
            functions,
            effects
        ),
        componentsRegister = nativeComponents
    )

    fun inflateComponentRender(componentType: String): ComponentRender<*> =
        componentsRegister[componentType]

}