package ru.javersingleton.bdui.engine

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.LambdaValue
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.entity.ComponentData
import ru.javersingleton.bdui.engine.field.entity.ComponentField
import ru.javersingleton.bdui.engine.field.entity.StructureData
import ru.javersingleton.bdui.engine.interaction.Interaction

class BeduinController(
    context: BeduinContext,
    state: ComponentField? = null,
    private val onInteraction: (controller: BeduinController, interaction: Interaction) -> Unit,
) : BeduinContext by context {

    private var lastState: ComponentField? = null
    private val lambda = Lambda("BeduinController", this)

    val root: Value<ComponentData> = LambdaValue(lambda)

    var state: ComponentField
        get() = lastState ?: throw IllegalStateException()
        set(value) {
            if (value == lastState) {
                return
            }

            lastState = value
            lambda.setBody (reason = "rootState changes") {
                // TODO Избавится от Nullable
                val args = rememberValue(
                    "controller@references",
                    value.componentType
                ) { MutableReferences() }.current!!
                val paramsField = value.params.resolve(this, args) as ResolvedField<StructureData>
                val paramsData =
                    (paramsField.value.current?.unbox() ?: mapOf()) + paramsField.dataWithUserId
                args.replace(this, paramsData)
                val componentField = ComponentField(
                    id = value.id,
                    componentType = value.componentType,
                    params = paramsField
                )
                val processedField = componentField.resolve(this, args)
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
        onInteraction(this, interaction)
    }
}

operator fun ComponentField.plus(target: Field<*>): ComponentField =
    mergeDeeply(targetFieldId = target.id, targetField = target)