package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.ComponentStructure
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.resolveThemselves

class BeduinController(
    state: ComponentField? = null
) {

    private var lastState: ComponentField? = null
    private val lambda = Lambda(MainBeduinContext())

    val root: Value<ComponentStructure> = LambdaValue(lambda)

    var state: ComponentField
        get() = lastState ?: throw IllegalStateException()
        set(value) {
            if (value == lastState) {
                return
            }

            lastState = value
            lambda.setBody {
                val componentField = ComponentField(
                    id = value.id,
                    componentType = value.componentType,
                    params = resolveThemselves("${value.id}@params", value.params)
                )
                val processedField = componentField.resolve(this, mutableMapOf())
                if (processedField !is ResolvedField) {
                    throw IllegalArgumentException()
                }

                processedField.value.current<ComponentStructure>()
            }
        }

    init {
        if (state != null) {
            this.state = state
        }
    }

}

operator fun ComponentField.plus(target: ComponentField): ComponentField =
    mergeDeeply(targetFieldId = target.id, targetField = target) as ComponentField