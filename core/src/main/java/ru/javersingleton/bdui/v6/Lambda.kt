package ru.javersingleton.bdui.v6

import kotlinx.coroutines.flow.*
import ru.javersingleton.bdui.v6.field.Structure
import ru.javersingleton.bdui.v6.field.Transform

abstract class Lambda : ReadableState {

    private var _value: Lazy<Any> = emptyValue()
    override val currentValue: Any get() = _value.value

    private var args: Map<String, State> = mapOf()
    private val cache: MutableMap<Any?, CachedState> = mutableMapOf()
    private var subscriptions: MutableMap<ReadableState, ReadableState.Subscription> = mutableMapOf()
    private val callbacks: MutableSet<(State) -> Unit> = mutableSetOf()

    private fun notifyStateChanged() {
        callbacks.forEach { callback ->
            callback(this)
        }
    }

    private fun invalidate() {
        _value = emptyValue()
        notifyStateChanged()
    }

    override fun subscribe(callback: (State) -> Unit): ReadableState.Subscription =
        Subscription(callback)

    fun setArguments(
        args: Map<String, State>
    ) {
        if (this.args != args) {
            invalidate()
        }
    }

    private fun privateInvoke(): Any = Call().invoke { invoke() }

    protected abstract fun Call.invoke(): Any

    inner class Call : Scope {
        fun invoke(func: Call.() -> Any): Any {
            val result = func()
            return result
        }

        override fun argument(name: String): State =
            (args[name] ?: throw IllegalArgumentException())

        override fun remember(
            callId: Any,
            key: Any?,
            func: Scope.() -> Any
        ): State {
            val cachedState = when {
                cache.containsKey(key) -> cache[key]
                else -> null
            }
            return if (cachedState?.input == key) {
                cachedState?.output ?: ConstState(null)
            } else {
                val newCachedState = CachedState(
                    key,
                    cachedState?.output.updateOrCreate(func())
                )
                cache[key] = newCachedState
                newCachedState.output
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <R> State.value(): R {
            val readableValue = this as ReadableState
            if (!subscriptions.containsKey(readableValue)) {
                subscriptions[readableValue] = this.subscribe {
                    invalidate()
                }
            }
            return readableValue.currentValue as R
        }

        override fun <T> toTyped(
            state: State,
            func: Structure.() -> T
        ): T = state.value<Structure>().func()

        override fun inflateComponent(componentType: String): ComponentLambda {
            TODO("Not yet implemented")
        }

        override fun inflateTransform(transformType: String): Transform {
            TODO("Not yet implemented")
        }

        private fun State?.updateOrCreate(value: Any): State =
            if (this is MutableState) {
                currentValue = value
                this
            } else {
                MutableState(value)
            }

    }

    inner class Subscription(
        private val callback: (State) -> Unit
    ) : ReadableState.Subscription {

        init {
            callbacks.add(callback)
        }

        override fun unsubscribe() {
            callbacks.remove(callback)
        }

    }

    private fun emptyValue(): Lazy<Any> = lazy { privateInvoke() }

    data class CachedState(
        val input: Any?,
        val output: State
    )

    interface Scope {

        fun argument(name: String): State

        fun remember(
            callId: Any,
            key: Any?,
            func: Scope.() -> Any
        ): State

        fun <T> State.value(): T

        fun <T> toTyped(
            state: State,
            func: Structure.() -> T
        ): T

        fun inflateComponent(componentType: String): ComponentLambda

        fun inflateTransform(transformType: String): Transform

    }

}