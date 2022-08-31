package ru.javersingleton.bdui.v3

interface State : FieldsHolder {

}

interface RawState {

    fun applyPatch(patch: StatePatch): RawState

    fun resolve(): State

}

data class StatePatch(
    private val fieldsHolder: FieldsHolder
) : FieldsHolder by fieldsHolder

data class RawStatePatch(
    private val structureField: StructureField
) : FieldsHolder by structureField {

    fun resolve(state: State): StatePatch =
        StatePatch(structureField.resolve(state) as ResolvedStructureField)

}

fun FieldsHolder.getComponentTemplate(refName: String): ComponentTemplate? =
    getStructureField(refName)?.let {
        ComponentTemplate(
            name = getString("name")!!,
            statePatch = RawStatePatch(
                getStructureField("statePatch")
                    ?: EmptyStructureField
            )
        )
    }

fun State.getComponentBlueprint(refName: String): ComponentBlueprint? =
    getComponentTemplate(refName)?.resolve(this)


data class ComponentTemplate(
    val name: String,
    val statePatch: RawStatePatch,
) {
    fun resolve(parentState: State): ComponentBlueprint =
        ComponentBlueprint(
            name = name,
            statePatch = statePatch.resolve(parentState)
        )

}

data class ComponentBlueprint(
    val name: String,
    val statePatch: StatePatch,
)

object EmptyState : RawState, State, FieldsHolder by EmptyStructureField {

    override fun applyPatch(patch: StatePatch): RawState {
        TODO("Not yet implemented")
    }

    override fun resolve(): State = this

}

