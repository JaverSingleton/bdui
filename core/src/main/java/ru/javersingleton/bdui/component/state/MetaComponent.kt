package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State
import ru.javersingleton.bdui.core.field.Field
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.Structure
import ru.javersingleton.bdui.core.field.StructureField

object MetaComponent {

    class StateFactory: ComponentState.Factory<MetaState>() {

        override fun Scope.create(componentType: String): MetaState {
            val defaultParams = inflateDefaultFields(componentType)
            val currentParams = defaultParams.mergeWith(args)

            val args: Structure? = resolveThemselves(
                id = defaultParams.id,
                params = currentParams
            ).state.value()

            val rootComponent = inflateRootComponent(componentType)
            val componentField = rootComponent.resolve(this, args?.unbox() ?: mapOf())
            return MetaState((componentField as ResolvedField).state.value())
        }

        private fun Lambda.Scope.resolveThemselves(
            id: String,
            params: Field?,
            args: Map<String, State<*>> = mapOf()
        ): ResolvedField {
            if (params == null) {
                return ResolvedField(id, rememberState(id, null) { null })
            }

            if (params is ResolvedField) {
                return params
            }

            val processedParams = params.resolve(this, args)
            return if (processedParams is ResolvedField) {
                processedParams
            } else {
                val newArgs = (processedParams as StructureField).extractStates()
                if (args == newArgs) {
                    throw IllegalArgumentException()
                }

                resolveThemselves(id, processedParams, newArgs)
            }
        }

    }

    data class MetaState(
        val childComponent: ComponentState
    )

}