package ru.javersingleton.bdui.v5

import kotlinx.coroutines.flow.StateFlow

class State(
    private val context: BeduinContext,
    private val componentType: String,
) {

    private val children: MutableMap<String, State> = mutableMapOf()
    // Нужно использовать обёртку которая будет хранить в себе связку Context и StructureField.
    // Это можно назвать оптимизированный вариант Field
    private var patchParams: Pair<StateContext, StructureField>? = null
    private var _defaultParams: Lazy<StructureField> = emptyLazyField()
    private val defaultParams: StructureField get() = _defaultParams.value

    private fun emptyLazyField(): Lazy<StructureField> = lazy { obtainState() }

    fun setupStatePatch(
        outerContext: StateContext,
        // Фактически сюда должен придти уже ResolvedStructureField
        // Это нужно чтобы компоненты лежащие внутри были привязаны к родительскому скоупу
        params: StructureField
    ) {
        // Если это первый вызов можно оставить до первого чтения состояния компонентом
        // При изменении состояния, родитель вызовет patch у StructureField со своим Context-ом
        // По мере прохождения по StateField будет вызываться их setupStatePatch
        // После patch состояния нотифицируем детей об изменённых полях
    }


    private fun obtainState(): StructureField {
        // Вызываем lazy когда нужно собрать состояние впервые
        // Использует собственный StateContext для создания вложенных State

        // Для NativeComponent field будет пустой
        // Для MetaComponent field будет хранить 2 поля: state и rootComponent
        val stateFieldParams = context.inflateStateField(componentType)
        setupState(stateFieldParams)
    }

    private fun setupState(params: StructureField) {

    }

    val stateChanges: StateFlow<Any>

    inner class LocalContext : StateContext, BeduinContext by context {

        override fun getState(field: StateField): State {
            val state = children[field.id]
            return if (state == null) {
                val newState = State(context, field.componentType)
                newState.setupStatePatch(
                    outerContext = this,
                    params = field.params
                )
                children[field.id] = newState
                newState
            } else {
                state
            }
        }

        // Для чего используется getField?
        // - Получение итогового значения при постройке State (будет использован в связке со своим Context)
        override fun getField(fieldName: String): Field? {
            val statePatch = patchParams
            if (statePatch != null) {
                val (outerContext, params) = statePatch
                if (params.hasField(fieldName)) {
                    return params.getField(fieldName)?.let { ResolvedField(outerContext, it) }
                }
            }

            if (defaultParams.hasField(fieldName)) {
                return defaultParams.getField(fieldName)
            }

            throw IllegalArgumentException("Такого поля нет в этом компоненте")
        }

        override fun hasField(fieldName: String): Boolean =
            patchParams?.second?.hasField(fieldName) == true || defaultParams.hasField(fieldName)

    }

}

interface StateContext : BeduinContext, FieldsContainer {

    fun getState(field: StateField): State

    override fun getField(fieldName: String): Field?

}