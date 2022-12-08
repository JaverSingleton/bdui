package ru.javersingleton.bdui.engine.interaction

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

interface Interaction {

    interface Factory : ByTypeStrategy.Element<String> {

        fun create(
            params: Map<String, Value<*>>
        ): Interaction

    }

}