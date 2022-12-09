package ru.javersingleton.bdui.engine.core

import android.util.Log
import ru.javersingleton.bdui.engine.BeduinContext
import ru.javersingleton.bdui.engine.field.EmptyData

class Lambda(
    private val tag: String,
    private val context: BeduinContext,
    private var script: Scope.() -> Any? = { throw IllegalStateException() }
) : ReadableValue<Any?>, ReadableValue.Subscription.Callback {

    override fun toString(): String {
        return "Lambda@$tag"
    }

    fun setBody(
        reason: String,
        script: Scope.() -> Any? = this.script,
    ) {
        if (this.script != script) {
            this.script = script
            val poster = ReadableValue.Subscription.BasePoster()
            onInvalidated(reason, poster)
            poster.invalidateAll()
        }
    }

    private var _value: Lazy<Any?> = emptyValue()
    override val currentValue: Any? get() = _value.value
    private var isValueCalculated = false

    private fun emptyValue(): Lazy<Any?> = lazy { invoke() }

    private var cache: Map<Any?, CachedState> = mapOf()

    private var subscriptions: Set<ReadableValue.Subscription> = setOf()
    private val subscribers: MutableSet<ReadableValue.Subscription.Callback> = mutableSetOf()
    private val endpointSubscribers: MutableSet<ReadableValue.Subscription.EndCallback> = mutableSetOf()

    override fun onInvalidated(reason: String, poster: ReadableValue.Subscription.Poster) {
        if (!isValueCalculated) {
            Log.d("Beduin-Invalidating", "Lambda $tag skipped invalidating called by $reason")
            return
        }
        Log.d("Beduin-Invalidating", "Lambda $tag invalidated by $reason")
        isValueCalculated = false
        _value = emptyValue()

        subscribers.forEach {
            it.onInvalidated(tag, poster)
        }
        endpointSubscribers.forEach {
            poster.postInvalidate(tag, it)
        }
    }

    override fun subscribe(callback: ReadableValue.Subscription.Callback): ReadableValue.Subscription =
        Subscription(callback)

    override fun subscribeEndpoint(callback: ReadableValue.Subscription.EndCallback): ReadableValue.Subscription =
        EndpointSubscription(callback)

    private fun invoke(): Any? {
        val call = Call()
        val result = call.script()
        Log.d("Beduin-Invalidating", "Lambda $tag calculated")
        isValueCalculated = true

        subscriptions.forEach { it.unsubscribe() }
        subscriptions = call.targetDependencies.map { it.subscribe(this) }.toSet()
        cache = call.targetCache

        return result
    }

    inner class Call : Scope, BeduinContext by context {
        private val cache = Cache(lastCache = this@Lambda.cache)
        val targetDependencies: MutableSet<ReadableValue<*>> = mutableSetOf()
        val targetCache: MutableMap<Any?, CachedState> get() = cache.targetCache

        override fun <T> rememberValue(
            callId: String,
            key: Any?,
            func: Scope.() -> T
        ): Value<T> {
            val cachedState = when {
                cache.containsKey(callId) -> cache[callId]
                else -> null
            }
            return if (cachedState != null && cachedState.input == key) {
                cachedState.output
            } else {
                val targetLambda = if (cachedState?.output is Lambda) {
                    cachedState.output.apply {
                        setBody("changes (${cachedState.input} -> $key)", func)
                    }
                } else {
                    Lambda(tag = callId, context).apply {
                        setBody("creating", func)
                    }
                }
                val newCachedState = CachedState(
                    key,
                    targetLambda
                )
                cache[callId] = newCachedState
                newCachedState.output
            }.let { LambdaValue(lambda = it) }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <R> Value<*>.current(): R? = current { null }

        @Suppress("UNCHECKED_CAST")
        override fun <R> Value<*>.current(default: (emptyData: EmptyData) -> R): R {
            val readableValue = this as ReadableValue
            targetDependencies.add(readableValue)
            val currentValue = readableValue.currentValue
            return if (currentValue !is EmptyData) {
                currentValue as R
            } else {
                default(currentValue)
            }
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
        private val callback: ReadableValue.Subscription.Callback
    ) : ReadableValue.Subscription {

        init {
            subscribers.add(callback)
        }

        override fun unsubscribe() {
            subscribers.remove(callback)
        }

    }

    inner class EndpointSubscription(
        private val callback: ReadableValue.Subscription.EndCallback
    ) : ReadableValue.Subscription {

        init {
            endpointSubscribers.add(callback)
        }

        override fun unsubscribe() {
            endpointSubscribers.remove(callback)
        }

    }

    data class CachedState(
        val input: Any?,
        val output: Lambda
    )

    interface Scope : BeduinContext {

        fun <T : Any?> rememberValue(
            callId: String,
            key: Any? = "",
            func: Scope.() -> T
        ): Value<T>

        val <T> Value<T>.current: T? get() = current()

        fun <T> Value<*>.current(): T?

        fun <T> Value<*>.current(default: (emptyData: EmptyData) -> T): T

    }

}