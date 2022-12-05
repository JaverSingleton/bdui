package ru.javersingleton.bdui.engine.interaction

import ru.javersingleton.bdui.engine.register.Register
import ru.javersingleton.bdui.engine.core.Value

interface Interaction {

    interface Factory : Register.Element {

        fun create(
            params: Map<String, Value<*>>
        ): Interaction

    }

}