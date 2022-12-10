package ru.javersingleton.bdui.engine.field.entity

import androidx.compose.runtime.Stable
import ru.javersingleton.bdui.engine.ArgumentsStorage
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.*

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
    ) : this(
        id = id ?: newId(),
        withUserId = id != null,
        componentType,
        params
    )

    override fun resolve(scope: Lambda.Scope, args: ArgumentsStorage): Field<ComponentData> =
        scope.run {
            val externalParamsField = params.resolve(scope, args)
            if (externalParamsField !is ResolvedField) {
                return copy(params = externalParamsField as StructureField)
            }

            val externalParams = externalParamsField.value

            val dataWithUserId: MutableMap<String, Value<*>> = mutableMapOf()
            dataWithUserId.putAll(externalParamsField.dataWithUserId)
            val resultValue = rememberValue(
                id,
                setOf(
                    componentType,
                    externalParams
                )
            ) {
                val stateFactory = rememberValue(
                    "$id@stateFactory",
                    componentType
                ) { inflateStateFactory(componentType) }

                ComponentData(
                    id = id,
                    withUserId = withUserId,
                    componentType = componentType,
                    params = externalParams.current { empty ->
                        StructureData(
                            id = empty.id,
                            withUserId = empty.withUserId,
                            fields = mapOf()
                        )
                    },
                    value = rememberValue(
                        "$id@value",
                        setOf(componentType, externalParams)
                    ) {
                        val externalParamsValue: StructureData = externalParams.current { empty ->
                            StructureData(
                                id = empty.id,
                                withUserId = empty.withUserId,
                                fields = mapOf()
                            )
                        }
                        stateFactory.current?.calculate(
                            scope = this,
                            args = externalParamsValue,
                            componentType = componentType
                        ) ?: throw IllegalArgumentException("StateFactory $componentType not found")
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
                else -> throw IllegalArgumentException("FieldType changing is forbidden for StatePatch")
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

@Stable
data class ComponentData(
    val id: String,
    val withUserId: Boolean,
    val componentType: String,
    val params: StructureData,
    val value: Value<*>
) : ResolvedData, PropertiesHolder by params {

    constructor(
        id: String? = null,
        componentType: String,
        params: StructureData,
        value: Value<*>
    ) : this(
        id = id ?: newId(),
        withUserId = id != null,
        componentType,
        params,
        value
    )

    override fun asField(): Field<ComponentData> = ComponentField(
        id = id.takeIf { withUserId } ?: newId(),
        withUserId = withUserId,
        componentType = componentType,
        params = params.asField()
    )

}