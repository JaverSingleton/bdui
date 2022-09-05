package ru.javersingleton.bdui.v3

import ru.javersingleton.bdui.v3.core.field.FieldsHolder

interface Field {

    fun resolve(source: FieldsHolder): Field?

}

interface ResolvedField : Field