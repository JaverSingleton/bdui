package ru.javersingleton.bdui.v3.core.component

import ru.javersingleton.bdui.v3.RawStatePatch
import ru.javersingleton.bdui.v3.State
import ru.javersingleton.bdui.v3.StatePatch
import ru.javersingleton.bdui.v3.core.field.FieldsHolder
import ru.javersingleton.bdui.v3.core.field.StructureField


fun FieldsHolder.getRawTemplate(refName: String): RawTemplate? =
    getStructureField(refName)?.let { RawTemplate(it) }

fun State.getTemplate(refName: String): Template? =
    getRawTemplate(refName)?.resolve(this)


data class RawTemplate(
    val name: String,
    val statePatch: RawStatePatch,
) {

    constructor(fieldsHolder: FieldsHolder): this(
        name = fieldsHolder.getString("name")!!,
        statePatch = RawStatePatch(
            fieldsHolder.getStructureField("statePatch")
                ?: StructureField.EMPTY
        )
    )

    fun resolve(parentState: State): Template =
        Template(
            name = name,
            statePatch = statePatch.resolve(parentState)
        )

}

data class Template(
    val name: String,
    val statePatch: StatePatch,
)