package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.interaction.Interaction

class BeduinController(
    context: BeduinContext,
    state: ComponentField? = null,
): BeduinContext by context {

    var onInteraction: ((Interaction) -> Unit)? = null
    private var lastState: ComponentField? = null
    private val lambda = Lambda(this)

    val root: Value<ComponentData> = LambdaValue(lambda)

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

                processedField.value.current<ComponentData>()
            }
        }

    init {
        if (state != null) {
            this.state = state
        }
    }

    override fun sendInteraction(interaction: Interaction) {
        onInteraction?.invoke(interaction)
    }
}

operator fun ComponentField.plus(target: Field<*>): ComponentField =
    mergeDeeply(targetFieldId = target.id, targetField = target) as ComponentField