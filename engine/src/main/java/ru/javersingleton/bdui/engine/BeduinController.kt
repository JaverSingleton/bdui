package ru.javersingleton.bdui.engine

import ru.javersingleton.bdui.engine.core.*
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.entity.ComponentData
import ru.javersingleton.bdui.engine.field.entity.ComponentField
import ru.javersingleton.bdui.engine.field.entity.PrimitiveData
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
                prepareComponentRoot(
                    scope = this,
                    id = value.id,
                    componentField = value
                ).current
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

    private fun prepareComponentRoot(
        scope: Scope,
        id: String,
        componentField: ComponentField
    ): Value<ComponentData> = scope.run {
        val args = rememberValue(
            "$id@references",
            componentField.componentType
        ) { MutableArgumentsStorage() }.current!!

        val resolvedStateField = componentField.params.resolve(this, args) as ResolvedField<StructureData>

        val stateProps = (resolvedStateField.value.current?.unbox() ?: mapOf())
        val markedProps = resolvedStateField.dataWithUserId

        val selfProp = mapOf(
            "self" to StructureData(
                "id" to PrimitiveData(value = id).asLazyValue()
            ).asLazyValue()
        )

        args.replace(
            scope = this,
            refs = stateProps + markedProps + selfProp
        )
        val resolvedComponentField = componentField.copy(
            params = resolvedStateField
        ).resolve(this, args) as ResolvedField
        resolvedComponentField.value
    }
}

operator fun ComponentField.plus(target: Field<*>): ComponentField =
    mergeDeeply(targetFieldId = target.id, targetField = target)