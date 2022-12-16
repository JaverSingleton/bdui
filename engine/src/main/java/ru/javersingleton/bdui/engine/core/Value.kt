package ru.javersingleton.bdui.engine.core

import androidx.compose.runtime.Stable
import ru.javersingleton.bdui.engine.field.entity.EmptyData

@Stable
interface Value<T : Any?> {

    object NULL : ImmutableValue<Any?>() {

        override val currentValue: Any? = null

    }

}

class ConstValue<T : Any?>(override val currentValue: T) : ImmutableValue<T>()

fun <T : Any?> T.asConstValue(): ConstValue<T> = ConstValue(this)

class CalculableValue<T : Any?>(
    private val script: () -> T
) : ImmutableValue<T>() {

    override val currentValue: T
        get() = script()
}

fun <T : Any?> T.asCalculableValue(): CalculableValue<T> = CalculableValue{ this }

class LazyValue<T : Any?>(
    script: () -> T
) : ImmutableValue<T>() {

    private val lazyValue: T by lazy { script() }

    override val currentValue: T
        get() = lazyValue

}

fun <T : Any?> T.asLazyValue(): LazyValue<T> = LazyValue{ this }

abstract class ImmutableValue<T> : ReadableValue<T> {

    override fun bindValidityWith(
        child: ReadableValue.Invalidatable,
        shouldDefer: Boolean
    ): ReadableValue.ValidityBond =
        object : ReadableValue.ValidityBond {

            override fun unbind() {
                // Do Nothing
            }

        }

}

@Stable
interface ReadableValue<T> : Value<T> {

    val currentValue: T

    fun bindValidityWith(child: Invalidatable, shouldDefer: Boolean = false): ValidityBond

    interface ValidityBond {

        fun unbind()

    }

    interface Invalidatable {

        fun onInvalidated(
            reason: String,
            postInvalidate: (reason: String, Invalidatable) -> Unit
        )

    }

}

data class LambdaValue<T : Any?>(private val lambda: Lambda) : ReadableValue<T> {

    @Suppress("UNCHECKED_CAST")
    override val currentValue: T
        get() = lambda.currentValue as T

    override fun bindValidityWith(
        child: ReadableValue.Invalidatable,
        shouldDefer: Boolean
    ): ReadableValue.ValidityBond =
        lambda.bindValidityWith(child, shouldDefer)

    override fun toString(): String {
        return lambda.toString()
    }
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