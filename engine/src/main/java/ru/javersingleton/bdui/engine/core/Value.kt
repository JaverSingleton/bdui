package ru.javersingleton.bdui.engine.core

import ru.javersingleton.bdui.engine.field.EmptyData

interface Value<T : Any?> {

    object NULL : ReadableValue<Any?> {

        override val currentValue: Any? = null

        override fun subscribe(callback: (Value<*>) -> Unit): ReadableValue.Subscription =
            object : ReadableValue.Subscription {

                override fun unsubscribe() {
                    // Do Nothing
                }

            }

    }

}

class ConstValue<T : Any?>(override val currentValue: T) : ReadableValue<T> {

    override fun subscribe(callback: (Value<*>) -> Unit): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

}

class LazyValue<T : Any?>(
    script: () -> T
) : ReadableValue<T> {

    private val lazyValue: T by lazy { script() }

    override fun subscribe(callback: (Value<*>) -> Unit): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

    override val currentValue: T
        get() = lazyValue

}

interface ReadableValue<T> : Value<T> {

    val currentValue: T

    fun subscribe(callback: (Value<*>) -> Unit): Subscription

    interface Subscription {

        fun unsubscribe()

    }

}

data class LambdaValue<T : Any?>(private val lambda: Lambda) : ReadableValue<T> {

    @Suppress("UNCHECKED_CAST")
    override val currentValue: T
        get() = lambda.currentValue as T

    override fun subscribe(callback: (Value<*>) -> Unit): ReadableValue.Subscription =
        lambda.subscribe(callback)

}

@Suppress("UNCHECKED_CAST")
fun <R> Value<*>.currentQuiet(): R? = currentQuiet { null }

@Suppress("UNCHECKED_CAST")
fun <R> Value<*>.currentQuiet(default: (emptyData: EmptyData) -> R): R {
    val readableValue = this as ReadableValue
    val currentValue = readableValue.currentValue
    return if (currentValue !is EmptyData) {
        currentValue as R
    } else {
        default(currentValue)
    }
}

val <T> Value<T>.currentQuiet: T? get() = currentQuiet()