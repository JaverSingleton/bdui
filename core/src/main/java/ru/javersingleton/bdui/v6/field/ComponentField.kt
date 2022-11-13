package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.Lambda
import ru.javersingleton.bdui.v6.State

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

        val externalParams = externalParamsField?.state?.value as Structure?

        return ResolvedField(
            id,
            rememberState(id, setOf(externalParams, componentType)) {
                val defaultParams = inflateDefaultFields(componentType)
                defaultParams.mergeWith(externalParams)
                val rootComponent = inflateRootComponent(componentType)
                val params = resolveThemselves("$id@params", defaultParams)
                val childArgs = params.state.value<Structure?>()?.unbox() ?: mapOf()
                val state = rootComponent?.resolve(this, childArgs)
            }
        )
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