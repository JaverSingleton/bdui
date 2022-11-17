package ru.javersingleton.bdui.core

interface Value<T : Any?>

interface ReadableValue<T> : Value<T> {

    val currentValue: T

    fun subscribe(callback: (Value<*>) -> Unit): Subscription

    interface Subscription {

        fun unsubscribe()

    }

}

class LambdaValue<T : Any?>(private val lambda: Lambda) : ReadableValue<T> {

    @Suppress("UNCHECKED_CAST")
    override val currentValue: T
        get() = lambda.currentValue as T

    override fun subscribe(callback: (Value<*>) -> Unit): ReadableValue.Subscription =
        lambda.subscribe(callback)

}

@Suppress("UNCHECKED_CAST")
internal fun <T : Any?> Value<T>.getValueQuiet(): T = (this as ReadableValue).currentValue