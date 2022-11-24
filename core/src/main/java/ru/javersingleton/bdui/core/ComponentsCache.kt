package ru.javersingleton.bdui.core

interface ComponentsCache {

    fun get(type: String): MetaComponentBlueprint?

    fun put(type: String, component: MetaComponentBlueprint)

}
