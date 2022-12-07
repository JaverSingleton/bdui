package ru.javersingleton.bdui.engine.register

interface Register<T : Register.Element, K> {

    operator fun get(key: K): T

    interface Element

}

interface MutableRegister<T : Register.Element, K> : Register<T, K> {

    fun register(
        vararg element: T,
        default: (type: K) -> T,
    )

    fun register(
        vararg element: T
    )

    fun clear()

    fun unregister(vararg element: T)

}

class DynamicKeyRegister<T : DynamicKeyRegister.Element<K>, K>(
    private val registerType: String
) : MutableRegister<T, K> {

    private val factories: MutableSet<T> = mutableSetOf()
    private val emptyHandler: (K) -> T = { key ->
        throw IllegalArgumentException("$registerType $key couldn't be processed")
    }
    private var defaultHandler: (K) -> T = emptyHandler

    val elements: List<T> get() = factories.toList()
    fun handleDefault(key: K): T = defaultHandler(key)

    override fun register(
        vararg element: T,
        default: (K) -> T,
    ) {
        this.defaultHandler = default
        factories.addAll(element)
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
        factories.removeAll(element.toSet())
    }

    override operator fun get(key: K): T =
        factories.firstOrNull { it.checkRelevant(key) } ?: defaultHandler(key)

    interface Element<K>: Register.Element {
        fun checkRelevant(key: K): Boolean
    }

}

class StaticKeyRegister<T : StaticKeyRegister.Element<K>, K>(
    private val registerType: String
) : MutableRegister<T, K> {

    private val factories: MutableMap<K, T> = mutableMapOf()
    private val emptyHandler: (K) -> T = { key ->
        throw IllegalArgumentException("$registerType $key haven't been registered")
    }
    private var defaultHandler: (K) -> T = emptyHandler

    val elements: List<T> get() = factories.values.toList()
    fun handleDefault(key: K): T = defaultHandler(key)

    override fun register(
        vararg element: T,
        default: (K) -> T,
    ) {
        this.defaultHandler = default
        val newElements = element.map { it.key to it }
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
            val factory = factories[it.key]
            if (factory == it) {
                factories.remove(it.key)
            }
        }
    }

    override operator fun get(key: K): T =
        factories[key] ?: defaultHandler(key)

    interface Element<K>: Register.Element {
        val key: K
    }

}