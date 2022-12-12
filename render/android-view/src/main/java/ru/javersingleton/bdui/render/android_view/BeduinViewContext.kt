package ru.javersingleton.bdui.render.android_view

import ru.javersingleton.bdui.engine.BeduinContext
import ru.javersingleton.bdui.engine.MainBeduinContext
import ru.javersingleton.bdui.engine.meta.MetaComponentsStorage
import ru.javersingleton.bdui.engine.register.FunctionsRegister
import ru.javersingleton.bdui.engine.register.InteractionsRegister

class BeduinViewContext(
    beduinContext: BeduinContext,
    private val componentsRegister: ComponentsRegister
) : BeduinContext by beduinContext {

    constructor(
        metaComponents: MetaComponentsStorage,
        nativeComponents: ComponentsRegister,
        functions: FunctionsRegister,
        effects: InteractionsRegister,
    ) : this(
        beduinContext = MainBeduinContext(
            metaComponents,
            nativeComponents.createComponentStatesRegister(),
            functions,
            effects
        ),
        componentsRegister = nativeComponents
    )

    fun inflateComponentFactory(componentType: String): ComponentFactory<*> =
        componentsRegister[componentType]

}