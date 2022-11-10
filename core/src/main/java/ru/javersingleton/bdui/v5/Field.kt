package ru.javersingleton.bdui.v5

interface Field {

    val id: String

    fun patch(field: Field): Field

    fun toValue(context: StateContext): ValueField?

}

interface StructureField: ValueField, FieldsContainer {

    override fun toValue(context: StateContext): ValueField = this

}

interface ListField: ValueField {

    override fun toValue(context: StateContext): ValueField = this

    fun getSize(): Int

    fun getField(position: Int): Field

}

interface PrimitiveField: ValueField {

    override fun toValue(context: StateContext): ValueField = this

}

interface StateField: ValueField {

    override fun toState(): StateField = this

    val params: StructureField

    val componentType: String


}

interface ReferenceField: Field {

    val refFieldName: String

    override fun toValue(context: StateContext): ValueField? =
        context.getField(refFieldName)?.toValue(context)

}

interface ValueField: Field {

    fun toInt(): Int

    fun toStructure(): StructureField

    fun toList(): ListField

    fun toState(): StateField

    override fun toValue(context: StateContext): ValueField? = this

}

interface FieldsContainer {

    fun getField(fieldName: String): Field?

    fun hasField(fieldName: String): Boolean

}