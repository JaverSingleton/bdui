package ru.javersingleton.bdui.engine.register

interface Register<T : Register.Element> {

    operator fun get(type: String): T

    interface Element {
        val type: String
    }

}

interface MutableRegister<T : Register.Element> : Register<T> {

    fun register(
        vararg element: T,
        default: (type: String) -> T,
    )

    fun register(
        vararg element: T
    )

    fun clear()

    fun unregister(vararg element: T)

}

class CommonRegister<T : Register.Element>(
    private val registerType: String
) : MutableRegister<T> {

    private val factories: MutableMap<String, T> = mutableMapOf()
    private val emptyHandler: (String) -> T = { type ->
        throw IllegalArgumentException("$registerType $type haven't been registered")
    }
    private var defaultHandler: (String) -> T = emptyHandler

    val elements: List<T> get() = factories.values.toList()
    fun handleDefault(type: String): T = defaultHandler(type)

    override fun register(
        vararg element: T,
        default: (String) -> T,
    ) {
        this.defaultHandler = default
        val newElements = element.map { it.type to it }
        if (newElements.size != element.size) {
            throw IllegalArgumentException("You must use only unique types of $registerType")
        }
        factories.putAll(
            newElements
        )
    }

    override fun register(
        vararg element: T
    ) = register(
        *element,
        default = defaultHandler,
    )

    override fun clear() {
        factories.clear()
    }

    override fun unregister(vararg element: T) {
        element.forEach {
            val factory = factories[it.type]
            if (factory == it) {
                factories.remove(it.type)
            }
        }
    }

    override operator fun get(type: String): T =
        factories[type] ?: defaultHandler(type)
}