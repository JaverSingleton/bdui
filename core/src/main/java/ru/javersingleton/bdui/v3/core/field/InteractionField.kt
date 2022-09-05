package ru.javersingleton.bdui.v3.core.field

import ru.javersingleton.bdui.v3.Field
import ru.javersingleton.bdui.v3.ResolvedField
import ru.javersingleton.bdui.v3.core.interaction.Interaction

data class InteractionField(
    override val name: String,
    override val type: String,
    override val params: StructureField?,
) : Field, Interaction, ResolvedField {

    constructor(fieldsHolder: FieldsHolder): this(
        type = fieldsHolder.extractType(),
        name = fieldsHolder.extractName(),
        params = fieldsHolder.getStructureField("params")
    )

    override fun resolve(source: FieldsHolder): Field {
        return this
    }

    companion object {

        private fun FieldsHolder.extractType(): String = when {
            hasField("actionName") -> "action"
            hasField("effectName") -> "effect"
            hasField("eventName") -> "event"
            else -> throw IllegalArgumentException()
        }

        private fun FieldsHolder.extractName() =
            getString("${extractType()}Name")!!
    }

}