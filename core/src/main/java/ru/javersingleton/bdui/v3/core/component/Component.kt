package ru.javersingleton.bdui.v3

import android.content.Context
import android.view.View
import ru.javersingleton.bdui.v3.core.component.RawTemplate
import ru.javersingleton.bdui.v3.core.component.Template

interface ComponentInflater {

    fun inflateComponent(template: RawTemplate): Component

    fun inflateComponent(name: String): Component

}

interface ViewComponentInflater : ComponentInflater {

    override fun inflateComponent(template: RawTemplate): ViewComponent

    override fun inflateComponent(name: String): ViewComponent

    val context: Context

}

interface Component {

    fun create(
        inflater: ComponentInflater,
        startStatePatch: StatePatch
    )

    fun render(
        statePatch: StatePatch
    )

}

abstract class ViewComponent(
    private val defaultState: RawState = RawState.EMPTY
) : Component {

    private lateinit var _view: Lazy<View>
    val view: View by _view

    private lateinit var inflater: ViewComponentInflater

    protected val context: Context get() = inflater.context

    override fun create(
        inflater: ComponentInflater,
        startStatePatch: StatePatch
    ) {
        this.inflater = inflater as ViewComponentInflater
        _view = lazy {
            val startState = defaultState.applyPatch(startStatePatch).resolve()
            onCreateView(context, startState)
        }
    }

    override fun render(statePatch: StatePatch) {
        onRenderState(defaultState.applyPatch(statePatch).resolve())
    }

    protected fun inflateChild(template: Template): ViewComponent =
        inflateChild(template.name, template.statePatch)

    protected fun inflateChild(name: String, statePatch: StatePatch): ViewComponent {
        val component = inflater.inflateComponent(name)
        component.create(inflater, statePatch)
        return component
    }

    protected abstract fun onCreateView(context: Context, emptyState: State): View

    protected abstract fun onRenderState(state: State)

}