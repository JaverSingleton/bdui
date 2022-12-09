package ru.javersingleton.bdui.engine.register

interface Register<T : Register.Element, K> {

    operator fun get(key: K): T

    interface Element

    interface AccessStrategy<T : Element, K> : MutableRegister<T, K> {
        val elements: List<T>
        fun handleDefault(key: K): T
    }

}

interface MutableRegister<T : Register.Element, K> : Register<T, K> {

    fun register(
        elements: List<T>,
        default: (type: K) -> T,
    )

    fun register(
        elements: List<T>
    )

    fun register(
        vararg element: T
    ) = register(
        elements = element.toList()
    )

    fun register(
        vararg element: T,
        default: (type: K) -> T,
    ) = register(
        elements = element.toList(),
        default = default
    )

    fun clear()

    fun unregister(elements: List<T>)

    @Suppress("unused")
    fun unregister(vararg element: T) {
        unregister(element.toList())
    }

}

class CommonRegister<T : Register.Element, K>(
    private val registerType: String,
    private val accessStrategy: Register.AccessStrategy<T, K>
) : MutableRegister<T, K> by accessStrategy {

    private val emptyHandler: (K) -> T = { key ->
        throw IllegalArgumentException("$registerType $key couldn't be processed")
    }

    val elements: List<T> get() = accessStrategy.elements
    fun handleDefault(key: K): T = accessStrategy.handleDefault(key)

    init {
        accessStrategy.register(
            elements = listOf(),
            default = emptyHandler
        )
    }

}

class ByRelevantStrategy<T : ByRelevantStrategy.Element<K>, K> :
    Register.AccessStrategy<T, K> {
    private val factories: MutableSet<T> = mutableSetOf()
    private lateinit var defaultHandler: (K) -> T

    override val elements: List<T> get() = factories.toList()
    override fun handleDefault(key: K): T = defaultHandler(key)

    override fun register(
        elements: List<T>,
        default: (K) -> T,
    ) {
        this.defaultHandler = default
        factories.addAll(elements)
    }

    override fun register(
        elements: List<T>
    ) = register(
        elements = elements,
        default = defaultHandler,
    )

    override fun clear() {
        factories.clear()
    }

    override fun unregister(elements: List<T>) {
        factories.removeAll(elements.toSet())
    }

    override operator fun get(key: K): T =
        factories.firstOrNull { it.checkRelevant(key) } ?: defaultHandler(key)

    interface Element<K> : Register.Element {
        fun checkRelevant(key: K): Boolean
    }

}

class ByTypeStrategy<T : ByTypeStrategy.Element<K>, K> : Register.AccessStrategy<T, K> {

    private val factories: MutableMap<K, T> = mutableMapOf()
    private lateinit var defaultHandler: (K) -> T

    override val elements: List<T> get() = factories.values.toList()

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun handleDefault(type: K): T = defaultHandler(type)

    override fun register(
        elements: List<T>,
        default: (K) -> T,
    ) {
        this.defaultHandler = default
        val newElements = elements.map { it.type to it }
        if (newElements.size != elements.size) {
            throw IllegalArgumentException("You must use only unique types")
        }
        factories.putAll(
            newElements
        )
    }

    override fun register(
        elements: List<T>
    ) = register(
        elements = elements,
        default = defaultHandler,
    )

    override fun clear() {
        factories.clear()
    }

    override fun unregister(elements: List<T>) {
        elements.forEach {
            val factory = factories[it.type]
            if (factory == it) {
                factories.remove(it.type)
            }
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override operator fun get(type: K): T =
        factories[type] ?: defaultHandler(type)

    interface Element<K> : Register.Element {
        val type: K
    }

}