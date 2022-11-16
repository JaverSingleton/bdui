package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

data class ComponentField(
    override val id: String,
    val componentType: String,
    val params: Field?
) : Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field = scope.run {
        val params = params as StructureField?

        val externalParamsField = params?.resolve(scope, args)
        if (externalParamsField !is ResolvedField?) {
            return ComponentField(id, componentType, externalParamsField)
        }

        val externalParamsState = externalParamsField?.state

        return ResolvedField(
            id,
            rememberState(id, componentType) {
                val stateFactory = rememberState("$id@stateFactory", componentType) {
                    inflateStateFactory(componentType)
                }

                ComponentStructure(
                    componentType = componentType,
                    params = externalParamsState?.value(),
                    state = rememberState("$id@value", componentType) {
                        val externalParams: Structure? = externalParamsState?.value()
                        stateFactory.value.calculate(
                            scope = this,
                            args = externalParams,
                            componentType = componentType
                        )
                    }
                )
            }
        )
    }

}

data class ComponentStructure(
    val componentType: String,
    val params: Structure?,
    val state: State<*>
)