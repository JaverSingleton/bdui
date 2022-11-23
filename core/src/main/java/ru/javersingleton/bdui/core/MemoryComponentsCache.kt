package ru.javersingleton.bdui.core

class MemoryComponentsCache : ComponentsCache {

    private val componentsMap = HashMap<String, MetaComponentBlueprint>()

    override fun get(type: String): MetaComponentBlueprint? = componentsMap[type]

    override fun put(type: String, component: MetaComponentBlueprint) {
        componentsMap[type] = component
    }

}