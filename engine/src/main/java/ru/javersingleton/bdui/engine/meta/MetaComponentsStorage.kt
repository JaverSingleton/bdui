package ru.javersingleton.bdui.engine.meta

interface MetaComponentsStorage {

    operator fun get(type: String): MetaComponentBlueprint?

    fun put(type: String, component: MetaComponentBlueprint)

}
