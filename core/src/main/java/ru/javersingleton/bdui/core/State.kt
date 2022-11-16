package ru.javersingleton.bdui.core

interface State<T : Any?>

interface ReadableState<T> : State<T> {

    val currentValue: T

    fun subscribe(callback: (State<*>) -> Unit): Subscription

    interface Subscription {

        fun unsubscribe()

    }

}

class LambdaState<T : Any?>(private val lambda: Lambda) : ReadableState<T> {

    @Suppress("UNCHECKED_CAST")
    override val currentValue: T
        get() = lambda.currentValue as T

    override fun subscribe(callback: (State<*>) -> Unit): ReadableState.Subscription =
        lambda.subscribe(callback)

}

@Suppress("UNCHECKED_CAST")
internal fun <T : Any?> State<T>.getValueQuiet(): T = (this as ReadableState).currentValue