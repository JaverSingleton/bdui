package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.References
import ru.javersingleton.bdui.core.Value

data class ComponentField(
    override val id: String,
    override val withUserId: Boolean,
    val componentType: String,
    val params: Field<StructureData>
) : Field<ComponentData> {

    constructor(
        id: String? = null,
        componentType: String,
        params: Field<StructureData>
    ) : this(id = id ?: newId(), withUserId = id != null, componentType, params)

    override fun resolve(scope: Lambda.Scope, args: References): Field<ComponentData> =
        scope.run {
            val externalParamsField = params.resolve(scope, args)
            if (externalParamsField !is ResolvedField) {
                return copy(params = externalParamsField as StructureField)
            }

            val externalParams = externalParamsField.value

            val dataWithUserId: MutableMap<String, Value<*>> = mutableMapOf()
            dataWithUserId.putAll(externalParamsField.dataWithUserId)
            val resultValue = rememberValue(id, componentType) {
                val stateFactory = rememberValue("$id@stateFactory", componentType) {
                    inflateStateFactory(componentType)
                }

                ComponentData(
                    id = id,
                    componentType = componentType,
                    params = externalParams.current { empty ->
                        StructureData(empty.id, fields = mapOf())
                    },
                    value = rememberValue("$id@value", componentType) {
                        val externalParams: StructureData =
                            externalParams.current { empty ->
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
            if (withUserId) {
                dataWithUserId[id] = resultValue
            }
            return ResolvedField(
                id,
                withUserId,
                resultValue,
                dataWithUserId
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(
        targetFieldId: String,
        targetField: Field<*>
    ): ComponentField {
        // TODO Переписать чтобы нормально обрабатывать смену componentType
        val targetParams = if (targetFieldId == id) {
            when (targetField) {
                is ComponentField -> {
                    if (targetField.componentType != componentType) {
                        throw IllegalArgumentException("Target ComponentType ${targetField.componentType} and Current ComponentType $componentType are different. ComponentType changing is forbidden for StatePatch")
                    }
                    params.mergeDeeply(params.id, targetField.params)
                }
                is StructureField -> {
                    params.mergeDeeply(params.id, targetField)
                }
                else -> {
                    throw IllegalArgumentException("FieldType changing is forbidden for StatePatch")
                }
            }
        } else {
            params.mergeDeeply(targetFieldId, targetField)
        }
        return if (targetParams != params) {
            this.copy(params = targetParams)
        } else {
            this
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

    override fun asField(): Field<ComponentData> = ComponentField(
        id = id,
        componentType = componentType,
        params = params.asField()
    )

}