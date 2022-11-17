package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class ComponentField(
    override val id: String,
    val componentType: String,
    val params: Field<Structure>
) : Field<ComponentStructure> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<ComponentStructure> = scope.run {
        val externalParamsField = params.resolve(scope, args)
        if (externalParamsField !is ResolvedField) {
            return ComponentField(id, componentType, externalParamsField as StructureField)
        }

        val externalParamsState = externalParamsField.value

        return ResolvedField(
            id,
            rememberValue(id, componentType) {
                val stateFactory = rememberValue("$id@stateFactory", componentType) {
                    inflateStateFactory(componentType)
                }

                ComponentStructure(
                    componentType = componentType,
                    params = externalParamsState.current(),
                    value = rememberValue("$id@value", componentType) {
                        val externalParams: Structure? = externalParamsState.current()
                        stateFactory.current.calculate(
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

fun ComponentField(
    type: String,
    vararg fields: Pair<String, Field<*>>
): ComponentField  =
    ComponentField(
        id = newId(),
        componentType = type,
        params = StructureField(*fields)
    )

data class ComponentStructure(
    val componentType: String,
    val params: Structure?,
    val value: Value<*>
)