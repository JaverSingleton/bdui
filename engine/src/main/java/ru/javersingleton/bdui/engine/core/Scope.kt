package ru.javersingleton.bdui.engine.core

import ru.javersingleton.bdui.engine.BeduinContext
import ru.javersingleton.bdui.engine.field.entity.EmptyData

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

class SimpleScope(context: BeduinContext) : Scope, BeduinContext by context {

    override fun <T> rememberValue(
        callId: String,
        key: Any?,
        func: Scope.() -> T
    ): Value<T> = LazyValue { func() }

    override fun <T> Value<*>.current(): T? =
        currentQuiet()

    override fun <T> Value<*>.current(default: (emptyData: EmptyData) -> T): T =
        currentQuiet(default)

}