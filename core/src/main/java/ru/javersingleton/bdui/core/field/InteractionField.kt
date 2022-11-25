package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.ConstValue
import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.getValueQuiet

data class InteractionField(
    override val id: String,
    private val params: Field<StructureData>,
    private val interactionType: String,
    private val interactionName: String,
) : Field<InteractionData> {

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, Value<*>>
    ): Field<InteractionData> = scope.run {
        val interaction = inflateInteraction(interactionType, interactionName)

        ResolvedField(
            id = id,
            value = rememberValue(
                id,
                setOf(args, interaction, params)
            ) {
                InteractionData(
                    raw = this@InteractionField
                ) { externalArgs ->
                    val callbackArgs = args + externalArgs
                    val resolvedParams = (params.resolve(this, callbackArgs) as ResolvedField)
                        .value.getValueQuiet()
//                    interaction.create(resolvedParams)
                    TODO()
                }
            }
        )
    }

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<InteractionData> {
        TODO("Not yet implemented")
    }

    override fun copyWithId(id: String): Field<InteractionData> {
        TODO("Not yet implemented")
    }


}

class InteractionData(
    private val raw: InteractionField,
    private val callback: (args: Map<String, Value<*>>) -> Unit
): ResolvedData {

    operator fun invoke(args: Map<String, Value<*>>) = callback.invoke(args)

    override fun toField(): Field<InteractionData> = raw

    inner class Arguments(
        private val values: MutableMap<String, Value<ResolvedData>> = mutableMapOf()
    ) {

        fun putString(name: String, value: String) {
            values[name] = ConstValue(PrimitiveData(newId(), value))
        }

        fun putInt(name: String, value: Int) {
            values[name] = ConstValue(PrimitiveData(newId(), value.toString()))
        }

    }

}

