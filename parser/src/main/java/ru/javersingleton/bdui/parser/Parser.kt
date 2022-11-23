package ru.javersingleton.bdui.parser

import ru.javersingleton.bdui.core.BeduinController
import ru.javersingleton.bdui.core.field.ComponentField
import java.io.Reader

interface Parser {

    fun parse(reader: Reader): BeduinController

    fun parseComponent(reader: Reader): ComponentField

}
