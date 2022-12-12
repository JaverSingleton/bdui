package ru.javersingleton.bdui.engine.core

import android.util.Log
import ru.javersingleton.bdui.engine.BeduinContext
import ru.javersingleton.bdui.engine.field.entity.EmptyData

class Lambda(
    private val id: String,
    private val context: BeduinContext,
    private var script: Scope.() -> Any? = { throw IllegalStateException() }
) : ReadableValue<Any?>, ReadableValue.Invalidatable {

    override fun toString(): String {
        return "Lambda@$id"
    }

    fun setBody(
        reason: String,
        script: Scope.() -> Any? = this.script,
    ) {
        if (this.script != script) {
            this.script = script
            val postponer = InvalidationPostponer()
            onInvalidated(
                reason = reason,
                postInvalidate = { childReason, child -> postponer.postInvalidate(childReason, child) }
            )
            postponer.invalidateAll()
        }
    }

    private var _value: Lazy<Any?> = emptyValue()
    override val currentValue: Any? get() = _value.value
    private var isValueCalculated = false

    private fun emptyValue(): Lazy<Any?> = lazy { invoke() }

    private var cache: Map<Any?, CachedState> = mapOf()

    private var validityDependencies: Set<ReadableValue.ValidityBond> = setOf()
    private val validityBondChildren: MutableSet<ReadableValue.Invalidatable> = mutableSetOf()
    private val deferredValidityBondChildren: MutableSet<ReadableValue.Invalidatable> =
        mutableSetOf()

    override fun onInvalidated(
        reason: String,
        postInvalidate: (String, ReadableValue.Invalidatable) -> Unit
    ) {
        if (!isValueCalculated) {
            Log.d("Beduin-Invalidating", "Lambda $id skipped invalidating called by $reason")
            return
        }
        Log.d("Beduin-Invalidating", "Lambda $id invalidated by $reason")
        isValueCalculated = false
        _value = emptyValue()

        validityBondChildren.forEach {
            it.onInvalidated(id, postInvalidate)
        }
        deferredValidityBondChildren.forEach {
            postInvalidate(id, it)
        }
    }

    override fun bindValidityWith(
        child: ReadableValue.Invalidatable,
        shouldDefer: Boolean
    ): ReadableValue.ValidityBond =
        if (shouldDefer) {
            DeferredValidityBond(child)
        } else {
            ValidityBond(child)
        }

    private fun invoke(): Any? {
        val call = Call()
        val result = call.script()
        Log.d("Beduin-Invalidating", "Lambda $id calculated")
        isValueCalculated = true

        validityDependencies.forEach { it.unbind() }
        validityDependencies = call.targetDependencies.map { it.bindValidityWith(this) }.toSet()
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
                        setBody(reason = "changes (${cachedState.input} -> $key)", func)
                    }
                } else {
                    Lambda(id = callId, context).apply {
                        setBody(reason = "creating", func)
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

    inner class ValidityBond(
        private val bondChild: ReadableValue.Invalidatable
    ) : ReadableValue.ValidityBond {

        init {
            validityBondChildren.add(bondChild)
        }

        override fun unbind() {
            validityBondChildren.remove(bondChild)
        }

    }

    inner class DeferredValidityBond(
        private val bondChild: ReadableValue.Invalidatable
    ) : ReadableValue.ValidityBond {

        init {
            deferredValidityBondChildren.add(bondChild)
        }

        override fun unbind() {
            deferredValidityBondChildren.remove(bondChild)
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

    class InvalidationPostponer {
        private val posts: MutableMap<ReadableValue.Invalidatable, List<String>> =
            mutableMapOf()

        fun postInvalidate(reason: String, callback: ReadableValue.Invalidatable) {
            posts[callback] = (posts[callback] ?: listOf()) + listOf(reason)
        }

        fun invalidateAll() {
            posts.forEach { (callback, reason) ->
                callback.onInvalidated(reason.joinToString(separator = " and ")) { _, _ ->
                    throw IllegalArgumentException("Deferring invalidation is forbidden while second step is running")
                }
            }
        }
    }

}