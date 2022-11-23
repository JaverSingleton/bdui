package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class ComponentField(
    override val id: String = newId(),
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
                        val externalParams: Structure = externalParamsState.current()
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

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<ComponentStructure> {
        return if (targetFieldId != id) {
            val targetParams = params.mergeDeeply(targetFieldId, targetField)
            if (targetParams != params) {
                this.copy(params = targetParams)
            } else {
                this
            }
        } else {
            if (targetField is ComponentField) {
                val changedParams = params.mergeDeeply(params.id, targetField.params)
                if (changedParams != params || targetField.componentType != this.componentType) {
                    copy(
                        params = changedParams,
                        componentType = targetField.componentType
                    )
                } else {
                    this
                }
            } else {
                targetField.copyWithId(id = id)
            } as Field<ComponentStructure>
        }
    }

    override fun copyWithId(id: String): Field<ComponentStructure> = copy(id = id)

}

fun ComponentField(
    type: String,
    vararg fields: Pair<String, Field<*>>,
    id: String = newId(),
): ComponentField  =
    ComponentField(
        id = id,
        componentType = type,
        params = StructureField(*fields)
    )

data class ComponentStructure(
    val componentType: String,
    val params: Structure,
    val value: Value<*>
)