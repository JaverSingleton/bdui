package ru.javersingleton.bdui.engine.field.entity

import ru.javersingleton.bdui.engine.ArgumentsStorage
import ru.javersingleton.bdui.engine.BeduinContext
import ru.javersingleton.bdui.engine.core.*
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.newId
import ru.javersingleton.bdui.engine.interaction.Interaction

data class InteractionField(
    override val id: String,
    override val withUserId: Boolean,
    private val interactionType: String,
    private val params: Field<StructureData>,
) : Field<InteractionData> {

    constructor(
        id: String? = null,
        interactionType: String,
        params: Field<StructureData>
    ) : this(id = id ?: newId(), withUserId = id != null, interactionType, params)

    override fun resolve(
        scope: Lambda.Scope,
        args: ArgumentsStorage
    ): Field<InteractionData> = scope.run {
        val interactionFactory = inflateInteractionFactory(interactionType)

        val resultValue = rememberValue(
            id,
            setOf(args, interactionFactory, params)
        ) {
            InteractionData(
                raw = this@InteractionField
            ) { externalArgs ->
                InteractionScope(this).run {
                    val callbackArgs = ArgumentsStorageWrapper(args, externalArgs)
                    val resolvedParamsField = (params.resolve(this, callbackArgs) as ResolvedField)
                    val resolvedParams = resolvedParamsField.value.current { StructureData(it.id) }
                    interactionFactory.create(resolvedParams.unbox())
                }
            }
        }
        ResolvedField(
            id = id,
            withUserId = withUserId,
            value = resultValue,
            dataWithUserId = if (withUserId) {
                mapOf(id to resultValue)
            } else {
                mapOf()
            }
        )
    }

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<InteractionData> {
//        if (targetFieldId == id) {
//            TODO("Реализовать mergeDeeply для InteractionField")
//        }
        return this
    }

    override fun copyWithId(id: String): Field<InteractionData> = copy(id = id)


}

class InteractionScope(context: BeduinContext) : Lambda.Scope, BeduinContext by context {

    override fun <T> rememberValue(
        callId: String,
        key: Any?,
        func: Lambda.Scope.() -> T
    ): Value<T> = LazyValue { func() }

    override fun <T> Value<*>.current(): T? =
        currentQuiet()

    override fun <T> Value<*>.current(default: (emptyData: EmptyData) -> T): T =
        currentQuiet(default)

}

class ArgumentsStorageWrapper(
    private val parent: ArgumentsStorage,
    private val newArgs: Map<String, Value<*>>
) : ArgumentsStorage {

    override fun get(refName: String): Value<Value<*>> =
        when {
            newArgs.containsKey(refName) -> ConstValue(newArgs[refName]!!)
            else -> parent[refName]
        }

}

class InteractionData(
    private val raw: InteractionField,
    private val callback: (args: Map<String, Value<*>>) -> Interaction
) : ResolvedData {

    operator fun invoke(args: Map<String, Value<*>>): Interaction = callback.invoke(args)

    override fun asField(): Field<InteractionData> = raw

}

