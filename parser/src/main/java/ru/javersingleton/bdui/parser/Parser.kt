package ru.javersingleton.bdui.parser

import ru.javersingleton.bdui.engine.field.Field
import java.io.Reader

interface Parser {

    fun parse(reader: Reader): Field<*>

}
