package ru.javersingleton.bdui.render.android_view

import android.view.View
import android.view.ViewGroup
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet

interface Component {

    val view: View

    fun createView(parent: ViewGroup)

    fun bindState(state: Value<*>)

}

abstract class BaseComponent<T, V : View> : Component {

    abstract val type: String

    private var _view: V? = null
    private var state: Value<T>? = null
    private var sub: Subscription? = null

    override val view: V
        get() = _view
            ?: throw IllegalStateException("Call createView before render for $type component")

    override fun createView(parent: ViewGroup) {
        _view = onCreateView(parent)
        this.state = null
        sub?.unsubscribe()
    }

    override fun bindState(state: Value<*>) {
        if (this.state == state) {
            return
        }

        @Suppress("UNCHECKED_CAST")
        val targetState = state as Value<T>
        this.state = targetState

        sub?.unsubscribe()
        sub = targetState.subscribe(type, view) {
            onBindState(view, it)
        }
        onBindState(view, targetState.currentQuiet!!)
    }

    abstract fun onCreateView(parent: ViewGroup): V

    abstract fun onBindState(view: V, state: T)

}