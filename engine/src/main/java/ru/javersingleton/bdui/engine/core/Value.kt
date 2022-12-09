package ru.javersingleton.bdui.engine.core

import androidx.compose.runtime.Stable
import ru.javersingleton.bdui.engine.field.EmptyData

@Stable
interface Value<T : Any?> {

    object NULL : ReadableValue<Any?> {

        override val currentValue: Any? = null

        override fun subscribe(callback: ReadableValue.Subscription.Callback): ReadableValue.Subscription =
            object : ReadableValue.Subscription {

                override fun unsubscribe() {
                    // Do Nothing
                }

            }

        override fun subscribeEndpoint(callback: ReadableValue.Subscription.EndCallback): ReadableValue.Subscription =
            object : ReadableValue.Subscription {

                override fun unsubscribe() {
                    // Do Nothing
                }

            }

    }

}

class ConstValue<T : Any?>(override val currentValue: T) : ReadableValue<T> {

    override fun subscribe(callback: ReadableValue.Subscription.Callback): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

    override fun subscribeEndpoint(callback: ReadableValue.Subscription.EndCallback): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

}

class DynamicValue<T : Any?>(
    private val script: () -> T
) : ReadableValue<T> {

    override val currentValue: T
        get() = script()

    override fun subscribe(callback: ReadableValue.Subscription.Callback): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

    override fun subscribeEndpoint(callback: ReadableValue.Subscription.EndCallback): ReadableValue.Subscription =
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

    override val currentValue: T
        get() = lazyValue

    override fun subscribe(callback: ReadableValue.Subscription.Callback): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

    override fun subscribeEndpoint(callback: ReadableValue.Subscription.EndCallback): ReadableValue.Subscription =
        object : ReadableValue.Subscription {

            override fun unsubscribe() {
                // Do Nothing
            }

        }

}

@Stable
interface ReadableValue<T> : Value<T> {

    val currentValue: T

    fun subscribe(callback: Subscription.Callback): Subscription

    fun subscribeEndpoint(callback: Subscription.EndCallback): Subscription

    interface Subscription {

        fun unsubscribe()

        interface Callback {

            fun onInvalidated(reason: String, poster: Poster)

        }

        class BasePoster: Poster {
            private val posts: MutableMap<EndCallback, List<String>> = mutableMapOf()

            override fun postInvalidate(reason: String, callback: EndCallback) {
                posts[callback] = (posts[callback] ?: listOf()) + listOf(reason)
            }

            fun invalidateAll() {
                posts.forEach { (callback, reason) ->
                    callback.onInvalidated(reason.joinToString(separator = " and "))
                }
            }
        }

        interface Poster {

            fun postInvalidate(reason: String, callback: EndCallback)

        }

        interface EndCallback {
            fun onInvalidated(reason: String)
        }

    }

}

data class LambdaValue<T : Any?>(private val lambda: Lambda) : ReadableValue<T> {

    @Suppress("UNCHECKED_CAST")
    override val currentValue: T
        get() = lambda.currentValue as T

    override fun subscribe(callback: ReadableValue.Subscription.Callback): ReadableValue.Subscription =
        lambda.subscribe(callback)

    override fun subscribeEndpoint(callback: ReadableValue.Subscription.EndCallback): ReadableValue.Subscription =
        lambda.subscribeEndpoint(callback)

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