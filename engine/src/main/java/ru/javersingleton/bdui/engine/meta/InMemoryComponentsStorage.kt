package ru.javersingleton.bdui.engine.meta

class InMemoryComponentsStorage : MetaComponentsStorage {

    private val componentsMap = HashMap<String, MetaComponentBlueprint>()

    override fun get(type: String): MetaComponentBlueprint? = componentsMap[type]

    override fun put(type: String, component: MetaComponentBlueprint) {
        componentsMap[type] = component
    }

}