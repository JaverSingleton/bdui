package ru.javersingleton.bdui.v3

import ru.javersingleton.bdui.v3.core.field.FieldsHolder
import ru.javersingleton.bdui.v3.core.field.ResolvedStructureField
import ru.javersingleton.bdui.v3.core.field.StructureField

data class State(
    private val fieldsHolder: ResolvedStructureField
) : FieldsHolder by fieldsHolder {

    companion object {
        val EMPTY = State(StructureField.EMPTY)
    }

}


data class RawState(
    private val structureField: StructureField
) {

    fun applyPatch(patch: StatePatch): RawState =
        RawState(structureField.mergeWith(patch.resolvedStructureField))

    fun resolve(): State =
        State(structureField.resolve(State.EMPTY) as ResolvedStructureField)

    companion object {
        val EMPTY = RawState(StructureField.EMPTY)
    }

}

data class StatePatch(
    internal val resolvedStructureField: ResolvedStructureField
) : FieldsHolder by resolvedStructureField

data class RawStatePatch(
    private val structureField: StructureField
) {

    fun resolve(state: State): StatePatch =
        StatePatch(structureField.resolve(state) as ResolvedStructureField)

}

