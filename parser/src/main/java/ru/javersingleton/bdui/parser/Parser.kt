package ru.javersingleton.bdui.parser

import ru.javersingleton.bdui.core.BeduinController
import ru.javersingleton.bdui.core.field.Field
import java.io.Reader

interface Parser {

    fun parse(reader: Reader): BeduinController

    fun parseObject(reader: Reader): Field<*>

}
