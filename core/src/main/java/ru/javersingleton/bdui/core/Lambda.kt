package ru.javersingleton.bdui.core

import kotlinx.coroutines.flow.*

class Lambda(
    private val context: BeduinContext,
    private var script: Scope.() -> Any? = { throw IllegalStateException() }
) : ReadableState<Any?> {


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

    private fun emptyValue(): Lazy<Any?> = lazy { invoke() }

    private var cache: Map<Any?, CachedState> = mapOf()

    private var subscriptions: List<ReadableState.Subscription> = listOf()
    private val subscribers: MutableSet<(State<*>) -> Unit> = mutableSetOf()

    private var isInvalidating = false
    // TODO Возможно стоить поддержать reInvalidation если считанная ранее переменная изменилась

    private fun notifyStateChanged() {
        if (isInvalidating) {
            return
        }

        isInvalidating = true
        subscribers.forEach { callback ->
            callback(this)
        }
    }

    private fun invalidate() {
        _value = emptyValue()
        notifyStateChanged()
    }

    override fun subscribe(callback: (State<*>) -> Unit): ReadableState.Subscription =
        Subscription(callback)

    private fun invoke(): Any? {
        val call = Call()
        val result = call.script()

        subscriptions.forEach { it.unsubscribe() }
        subscriptions = call.targetDependencies.map { it.subscribe { invalidate() } }
        cache = call.targetCache

        return result
    }

    inner class Call : Scope, BeduinContext by context {
        private val cache = Cache(lastCache = this@Lambda.cache)
        val targetDependencies: MutableSet<ReadableState<*>> = mutableSetOf()
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
            targetDependencies.add(readableValue)
            return readableValue.currentValue as R
        }

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
                lastCache.containsKey(key) -> lastCache[key]?.apply {
                    targetCache[key] = this
                }
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
            subscribers.add(callback)
        }

        override fun unsubscribe() {
            subscribers.remove(callback)
        }

    }

    data class CachedState(
        val input: Any?,
        val output: Lambda
    )

    interface Scope : BeduinContext {

        fun <T : Any?> rememberState(
            callId: String,
            key: Any? = "",
            func: Scope.() -> T
        ): State<T>

        val <T> State<T>.value: T get() = value()

        fun <T> State<*>.value(): T

    }

}