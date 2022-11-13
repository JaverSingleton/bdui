package ru.javersingleton.bdui.v6

import kotlinx.coroutines.flow.*
import ru.javersingleton.bdui.v6.field.Structure

class Lambda(private val context: BeduinContext) : ReadableState<Any?> {

    private var script: Scope.() -> Any? = { throw IllegalStateException() }

    fun setBody(
        script: Scope.() -> Any? = this.script
    ) {
        if (this.script != script) {
            this.script = script
            invalidate()
        }
    }

    private var _value: Lazy<Any?> = emptyValue()
    override val currentValue: Any? get() = _value.value

    private fun emptyValue(): Lazy<Any?> = lazy { privateInvoke() }

    private var cache: Map<Any?, CachedState> = mapOf()
    private var dependencies: Map<State<*>, ReadableState.Subscription> = mapOf()

    private val changesListeners: MutableSet<(State<*>) -> Unit> = mutableSetOf()

    private fun notifyStateChanged() {
        changesListeners.forEach { callback ->
            callback(this)
        }
    }

    private fun invalidate() {
        _value = emptyValue()
        notifyStateChanged()
    }

    override fun subscribe(callback: (State<*>) -> Unit): ReadableState.Subscription =
        Subscription(callback)

    private fun privateInvoke(): Any? {
        val call = Call()
        val result = call.script()

        dependencies.values.forEach { it.unsubscribe() }
        dependencies = call.targetDependencies
        cache = call.targetCache

        return result
    }

    inner class Call : Scope, BeduinContext by context {
        private val cache = Cache(lastCache = this@Lambda.cache)
        val targetDependencies: MutableMap<State<*>, ReadableState.Subscription> = mutableMapOf()
        val targetCache: MutableMap<Any?, CachedState> get() = cache.targetCache

        override fun <T> rememberState(
            callId: String,
            key: Any?,
            func: Scope.() -> T
        ): State<T> {
            val cachedState = when {
                cache.containsKey(callId) -> cache[callId]
                else -> null
            }
            return if (cachedState != null && cachedState.input != key) {
                cachedState.output
            } else {
                val newCachedState = CachedState(
                    key,
                    if (cachedState?.output is Lambda) {
                        cachedState.output.apply {
                            setBody(func)
                        }
                    } else {
                        Lambda(context).apply {
                            setBody(func)
                        }
                    }
                )
                cache[callId] = newCachedState
                newCachedState.output
            }.let { LambdaState(lambda = it) }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <R> State<*>.value(): R {
            val readableValue = this as ReadableState
            if (!targetDependencies.containsKey(readableValue)) {
                targetDependencies[readableValue] = this.subscribe {
                    invalidate()
                }
            }
            return readableValue.currentValue as R
        }

        override fun <T> toTyped(
            state: State<*>,
            func: Structure.() -> T
        ): T = state.value<Structure>().func()

    }

    class Cache(
        private val lastCache: Map<Any?, CachedState>,
        val targetCache: MutableMap<Any?, CachedState> = mutableMapOf(),
    ) {

        fun containsKey(key: Any?): Boolean =
            targetCache.containsKey(key) || lastCache.containsKey(key)

        operator fun get(key: Any?): CachedState? =
            when {
                targetCache.containsKey(key) -> targetCache[key]
                lastCache.containsKey(key) -> lastCache[key]
                else -> null
            }

        operator fun set(key: Any?, value: CachedState) {
            targetCache[key] = value
        }
    }

    inner class Subscription(
        private val callback: (State<*>) -> Unit
    ) : ReadableState.Subscription {

        init {
            changesListeners.add(callback)
        }

        override fun unsubscribe() {
            changesListeners.remove(callback)
        }

    }

    data class CachedState(
        val input: Any?,
        val output: Lambda
    )

    interface Scope : BeduinContext {

        fun <T : Any?> rememberState(
            callId: String,
            key: Any?,
            func: Scope.() -> T
        ): State<T>

        val <T> State<T>.value: T get() = value()

        fun <T> State<*>.value(): T

        fun <T> toTyped(
            state: State<*>,
            func: Structure.() -> T
        ): T

    }

}