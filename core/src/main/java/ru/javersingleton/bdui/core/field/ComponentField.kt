package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class ComponentField(
    override val id: String,
    override val withUserId: Boolean,
    val componentType: String,
    val params: Field<StructureData>
) : Field<ComponentData> {

    constructor(
        id: String,
        componentType: String,
        params: Field<StructureData>
    ) : this(id = id, withUserId = true, componentType, params)

    constructor(
        componentType: String,
        params: Field<StructureData>
    ) : this(id = newId(), withUserId = false, componentType, params)

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<ComponentData> =
        scope.run {
            val externalParamsField = params.resolve(scope, args)
            if (externalParamsField !is ResolvedField) {
                return ComponentField(id, componentType, externalParamsField as StructureField)
            }

            val externalParamsState = externalParamsField.value

            return ResolvedField(
                id,
                withUserId,
                rememberValue(id, componentType) {
                    val stateFactory = rememberValue("$id@stateFactory", componentType) {
                        inflateStateFactory(componentType)
                    }

                    ComponentData(
                        id = id,
                        componentType = componentType,
                        params = externalParamsState.current { empty ->
                            StructureData(empty.id, fields = mapOf())
                        },
                        value = rememberValue("$id@value", componentType) {
                            val externalParams: StructureData =
                                externalParamsState.current { empty ->
                                    StructureData(empty.id, fields = mapOf())
                                }
                            stateFactory.current?.calculate(
                                scope = this,
                                args = externalParams,
                                componentType = componentType
                            )
                                ?: throw IllegalArgumentException("StateFactory $componentType not found")
                        }
                    )
                }
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(
        targetFieldId: String,
        targetField: Field<*>
    ): ComponentField {
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
            } as ComponentField
        }
    }

    override fun copyWithId(id: String): Field<ComponentData> = copy(id = id)

}

data class ComponentData(
    val id: String,
    val componentType: String,
    val params: StructureData,
    val value: Value<*>
) : ResolvedData {

    override fun toField(): Field<ComponentData> = ComponentField(
        id = id,
        componentType = componentType,
        params = params.toField()
    )

}