package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.ComponentStructure
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.resolveThemselves

class Beduin {

    private var lastState: ComponentField? = null
    private val lambda = Lambda(MainBeduinContext())

    val currentState: ComponentField get() = lastState ?: throw IllegalStateException()
    val root: Value<ComponentStructure> = LambdaValue(lambda)

    fun setState(
        state: ComponentField
    ) {
        if (state == lastState) {
            return
        }

        lastState = state
        lambda.setBody {
            val componentField = ComponentField(
                id = state.id,
                componentType = state.componentType,
                params = resolveThemselves("${state.id}@params", state.params)
            )
            val processedField = componentField.resolve(this, mutableMapOf())
            if (processedField !is ResolvedField) {
                throw IllegalArgumentException()
            }

            processedField.value.current<ComponentStructure>()
        }
    }

}