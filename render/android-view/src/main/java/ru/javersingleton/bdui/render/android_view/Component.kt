package ru.javersingleton.bdui.render.android_view

import android.view.View
import android.view.ViewGroup
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet

interface Component {

    fun createOrUpdateView(
        parent: ViewGroup,
        state: Value<*>
    ): View

}

abstract class BaseComponent<T, V : View> : Component {

    abstract val type: String

    private var state: Value<T>? = null
    private var sub: Subscription? = null

    private var _view: V? = null
    private val view: V
        get() = _view!!

    override fun createOrUpdateView(
        parent: ViewGroup,
        state: Value<*>
    ): View {
        if (_view == null) {
            _view = onCreateView(parent)
            this.state = null
            sub?.unsubscribe()
        }

        if (this.state == state) {
            return view
        }

        @Suppress("UNCHECKED_CAST")
        val targetState = state as Value<T>
        this.state = targetState

        sub?.unsubscribe()
        sub = targetState.subscribe(type, view) {
            onBindState(view, it)
        }
        onBindState(view, targetState.currentQuiet!!)
        return view
    }

    abstract fun onCreateView(parent: ViewGroup): V

    abstract fun onBindState(view: V, state: T)

}