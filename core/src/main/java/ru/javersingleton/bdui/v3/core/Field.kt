package ru.javersingleton.bdui.v3

interface Field {

    fun resolve(source: FieldsHolder): Field?

}

interface ResolvedField: Field

class ReferenceField(private val refFieldName: String) : Field {

    override fun resolve(source: FieldsHolder): Field? = source.getField(refFieldName)

}

data class ValueField(private val value: String) : Field, ResolvedField {

    override fun resolve(source: FieldsHolder): Field = this

    fun getStringValue(): String = value

    fun getIntValue(): Int = value.toInt()

    fun getBooleanValue(): Boolean = value.toBoolean()

}

fun Field.toStringValue(): String = (this as ValueField).getStringValue()
fun Field.toIntValue(): Int = (this as ValueField).getIntValue()
fun Field.toBooleanValue(): Boolean = (this as ValueField).getBooleanValue()
fun Field.toStructureField(): StructureField = (this as StructureField)

interface StructureField : Field, FieldsHolder

data class RawStructureField(
    private val fields: Map<String, Field?>
) : StructureField {

    private val isResolvable: Boolean = getBoolean("_resolvable") ?: true

    override fun getString(fieldName: String): String? = getField(fieldName)?.toStringValue()

    override fun getBoolean(fieldName: String): Boolean? = getField(fieldName)?.toBooleanValue()

    override fun getInt(fieldName: String): Int? = getField(fieldName)?.toIntValue()

    override fun getStructureField(fieldName: String): StructureField? = getField(fieldName)?.toStructureField()

    override fun getField(fieldName: String): Field? {
        return fields[fieldName]
    }

    override fun hasField(fieldName: String): Boolean {
        return fields.containsKey(fieldName)
    }

    override fun resolve(source: FieldsHolder): Field {
        if (!isResolvable) {
            return ResolvedStructureField(this)
        }

        val builder = StructureFieldBuilder(source)
        var checkableFields = fields
        while (checkableFields.isNotEmpty()) {
            val unresolvedFields = mutableMapOf<String, Field?>()
            checkableFields.forEach { (name, field) ->
                val resolvedField = field?.resolve(builder)
                if (resolvedField is ResolvedField?) {
                    builder.addResolvedField(name, resolvedField)
                } else {
                    unresolvedFields[name] = field
                }
            }
            if (checkableFields == unresolvedFields) {
                return this
            }
            checkableFields = unresolvedFields.toMap()
        }

        return ResolvedStructureField(builder.build())
    }

}

data class ResolvedStructureField(
    private val structureField: StructureField
) : StructureField by structureField, ResolvedField {

    override fun resolve(source: FieldsHolder): Field = this

}

data class StructureFieldBuilder(
    private val source: FieldsHolder
) : StructureField, ResolvedField {

    private val resolvedFields: MutableMap<String, ResolvedField?> = mutableMapOf()

    fun addResolvedField(fieldName: String, resolvedField: ResolvedField?) {
        resolvedFields[fieldName] = resolvedField
    }

    fun build(): StructureField = ResolvedStructureField(RawStructureField(resolvedFields))

    override fun getString(fieldName: String): String? = getField(fieldName)?.toStringValue()

    override fun getBoolean(fieldName: String): Boolean? = getField(fieldName)?.toBooleanValue()

    override fun getInt(fieldName: String): Int? = getField(fieldName)?.toIntValue()

    override fun getStructureField(fieldName: String): StructureField? = getField(fieldName)?.toStructureField()

    override fun getField(fieldName: String): Field? =
        if (resolvedFields.containsKey(fieldName)) {
            resolvedFields[fieldName]
        } else {
            source.getField(fieldName)
        }

    override fun hasField(fieldName: String): Boolean =
        resolvedFields.containsKey(fieldName) || source.hasField(fieldName)

    override fun resolve(source: FieldsHolder): Field = this

}

object EmptyStructureField : StructureField, ResolvedField {

    override fun resolve(source: FieldsHolder): Field = this

    override fun getInt(fieldName: String): Int? = null

    override fun getStructureField(fieldName: String): StructureField? = null

    override fun getString(fieldName: String): String? = null

    override fun getBoolean(fieldName: String): Boolean? = null

    override fun getField(fieldName: String): Field? = null

    override fun hasField(fieldName: String): Boolean = false

}

interface FieldsHolder {

    fun getString(fieldName: String): String?

    fun getBoolean(fieldName: String): Boolean?

    fun getInt(fieldName: String): Int?

    fun getStructureField(fieldName: String): StructureField?

    fun getField(fieldName: String): Field?

    fun hasField(fieldName: String): Boolean

}