package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.interaction.Interaction

data class InteractionField(
    override val id: String,
    override val withUserId: Boolean,
    private val interactionType: String,
    private val interactionName: String,
    private val params: Field<StructureData>,
) : Field<InteractionData> {

    constructor(
        id: String,
        interactionType: String,
        interactionName: String,
        params: Field<StructureData>
    ) : this(id = id, withUserId = true, interactionType, interactionName, params)

    constructor(
        interactionType: String,
        interactionName: String,
        params: Field<StructureData>
    ) : this(id = newId(), withUserId = false, interactionType, interactionName, params)

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, Value<*>>
    ): Field<InteractionData> = scope.run {
        val interactionFactory = inflateInteractionFactory(interactionType, interactionName)

        ResolvedField(
            id = id,
            withUserId,
            value = rememberValue(
                id,
                setOf(args, interactionFactory, params)
            ) {
                InteractionData(
                    raw = this@InteractionField
                ) { externalArgs ->
                    val callbackArgs = args + externalArgs
                    val resolvedParams = (params.resolve(this, callbackArgs) as ResolvedField)
                        .value.current { StructureData(it.id) }
                    interactionFactory.create(resolvedParams.unbox())
                }
            }
        )
    }

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<InteractionData> {
        if (targetFieldId == id) {
            TODO("Not implemented yet")
        }
        return this
    }

    override fun copyWithId(id: String): Field<InteractionData> = copy(id = id)


}

class InteractionData(
    private val raw: InteractionField,
    private val callback: (args: Map<String, Value<*>>) -> Interaction
) : ResolvedData {

    operator fun invoke(args: Map<String, Value<*>>): Interaction = callback.invoke(args)

    override fun toField(): Field<InteractionData> = raw

}

