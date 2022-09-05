package ru.javersingleton.bdui.v3.core.field

import ru.javersingleton.bdui.v3.Field
import ru.javersingleton.bdui.v3.ResolvedField

interface StructureField : Field, FieldsHolder {

    val fields: Map<String, Field?>

    fun mergeWith(structureField: StructureField): StructureField

    companion object {
        val EMPTY = ResolvedStructureField(RawStructureField(mapOf()))
    }

}

interface FieldsHolder {

    fun getString(fieldName: String): String?

    fun getBoolean(fieldName: String): Boolean?

    fun getInt(fieldName: String): Int?

    fun getStructureField(fieldName: String): StructureField?

    fun getField(fieldName: String): Field?

    fun hasField(fieldName: String): Boolean

}

data class RawStructureField(
    override val fields: Map<String, Field?>
) : StructureField {

    override fun getString(fieldName: String): String? = getField(fieldName)?.toStringValue()

    override fun getBoolean(fieldName: String): Boolean? = getField(fieldName)?.toBooleanValue()

    override fun getInt(fieldName: String): Int? = getField(fieldName)?.toIntValue()

    override fun getStructureField(fieldName: String): StructureField? =
        getField(fieldName)?.toStructureField()

    override fun getField(fieldName: String): Field? {
        return fields[fieldName]
    }

    override fun hasField(fieldName: String): Boolean {
        return fields.containsKey(fieldName)
    }

    override fun mergeWith(structureField: StructureField): StructureField =
        RawStructureField(fields + structureField.fields)

    override fun resolve(source: FieldsHolder): Field {
        val builder = StructureFieldBuilder(source)
        var checkableFields = fields
        while (checkableFields.isNotEmpty()) {
            val unresolvedFields = mutableMapOf<String, Field?>()
            checkableFields.forEach { (name, field) ->
                val resolvingField = field?.resolve(builder)
                if (resolvingField is ResolvedField?) {
                    builder.addField(name, resolvingField)
                } else {
                    unresolvedFields[name] = resolvingField
                }
            }
            if (checkableFields == unresolvedFields) {
                builder.addFields(checkableFields)
                return builder.build()
            }
            checkableFields = unresolvedFields.toMap()
        }

        return ResolvedStructureField(builder.build())
    }

}

data class ResolvedStructureField(
    private val structureField: StructureField
) : StructureField by structureField,
    ResolvedField {

    override fun resolve(source: FieldsHolder): Field = this

}

data class StructureFieldBuilder(
    private val source: FieldsHolder
) : FieldsHolder {

    private val fields: MutableMap<String, Field?> = mutableMapOf()

    fun addField(fieldName: String, newField: ResolvedField?) {
        fields[fieldName] = newField
    }

    fun addFields(newFields: Map<String, Field?>) {
        fields.putAll(newFields)
    }

    fun build(): StructureField = RawStructureField(fields)

    override fun getString(fieldName: String): String? = getField(fieldName)?.toStringValue()

    override fun getBoolean(fieldName: String): Boolean? = getField(fieldName)?.toBooleanValue()

    override fun getInt(fieldName: String): Int? = getField(fieldName)?.toIntValue()

    override fun getStructureField(fieldName: String): StructureField? =
        getField(fieldName)?.toStructureField()

    override fun getField(fieldName: String): Field? =
        if (fields.containsKey(fieldName)) {
            fields[fieldName]
        } else {
            source.getField(fieldName)
        }

    override fun hasField(fieldName: String): Boolean =
        fields.containsKey(fieldName) || source.hasField(fieldName)

}