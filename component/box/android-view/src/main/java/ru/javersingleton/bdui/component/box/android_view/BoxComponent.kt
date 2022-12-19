package ru.javersingleton.bdui.component.box.android_view

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import ru.javersingleton.bdui.component.box.state.BoxState
import ru.javersingleton.bdui.render.android_view.BaseComponent
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.InnerComponent
import ru.javersingleton.bdui.render.android_view.replaceIfNeeded

class BoxComponent(
    private val beduinContext: BeduinViewContext
) : BaseComponent<BoxState, FrameLayout>() {

    override val type: String = "Box"
    private val children: MutableMap<String, BoxChild> = mutableMapOf()

    override fun onCreateView(parent: ViewGroup): FrameLayout = FrameLayout(parent.context)

    // TODO Переписать выставление состояние, сейчас не меняется layoutParams и позиция
    override fun onBindState(view: FrameLayout, state: BoxState) {
        with(view) {
            state.onClick?.let { callback ->
                setOnClickListener { callback() }
            }
            state.children.value.forEachIndexed { index, child ->
                val targetChild = getOrCreate(child)
                replaceIfNeeded(
                    index,
                    targetChild.component.createOrUpdateView(view, child.component),
                    targetChild.layoutParams.second
                )
            }
        }
    }

    private fun getOrCreate(childState: BoxState.Child): BoxChild {
        val targetChild = children[childState.component.id]?.let { currentChild ->
            if (currentChild.layoutParams.first == childState.params) {
                currentChild
            } else {
                currentChild.copy(
                    layoutParams = childState.params to createLayoutParams(childState.params)
                )
            }
        } ?: BoxChild(
            layoutParams = childState.params to createLayoutParams(childState.params),
            component = InnerComponent(beduinContext)
        )
        children[childState.component.id] = targetChild
        return targetChild
    }

}

data class BoxChild(
    val layoutParams: Pair<BoxState.Child.Params, LinearLayout.LayoutParams>,
    val component: InnerComponent
)

fun createLayoutParams(params: BoxState.Child.Params) = LinearLayout.LayoutParams(
    parseSizeDp(params.width),
    parseSizeDp(params.height)
).apply {
    setMargins(
        params.padding?.start ?: 0,
        params.padding?.top ?: 0,
        params.padding?.end ?: 0,
        params.padding?.bottom ?: 0,
    )
}

fun parseSizeDp(value: String): Int = when (value) {
    "fillMax" -> ViewGroup.LayoutParams.MATCH_PARENT
    "wrapContent" -> ViewGroup.LayoutParams.WRAP_CONTENT
    // TODO Implement fix size supporting
    else -> throw IllegalArgumentException("Couldn't parse size ")
}