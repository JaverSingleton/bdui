package ru.javersingleton.bdui.v3.core.field

import ru.javersingleton.bdui.v3.Field


class ReferenceField(private val refFieldName: String) : Field {

    override fun resolve(source: FieldsHolder): Field? =
        if (!source.hasField(refFieldName)) {
            this
        } else {
            source.getField(refFieldName)
        }

}