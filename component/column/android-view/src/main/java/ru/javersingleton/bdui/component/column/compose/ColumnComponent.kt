package ru.javersingleton.bdui.component.column.compose

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import ru.javersingleton.bdui.component.column.state.ColumnState
import ru.javersingleton.bdui.render.android_view.BaseComponent
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.InnerComponent
import ru.javersingleton.bdui.render.android_view.replaceIfNeeded

class ColumnComponent(
    private val beduinContext: BeduinViewContext
) : BaseComponent<ColumnState, LinearLayout>() {

    override val type: String = "Column"
    private val children: MutableMap<String, ColumnChild> = mutableMapOf()

    override fun onCreateView(parent: ViewGroup): LinearLayout =
        LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
        }

    // TODO Переписать выставление состояние, сейчас не меняется layoutParams и позиция
    override fun onBindState(view: LinearLayout, state: ColumnState) {
        with(view) {
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

    private fun getOrCreate(childState: ColumnState.Child): ColumnChild {
        val targetChild = children[childState.component.id]?.let { currentChild ->
            if (currentChild.layoutParams.first == childState.params) {
                currentChild
            } else {
                currentChild.copy(
                    layoutParams = childState.params to createLayoutParams(childState.params)
                )
            }
        } ?: ColumnChild(
            layoutParams = childState.params to createLayoutParams(childState.params),
            component = InnerComponent(beduinContext)
        )
        children[childState.component.id] = targetChild
        return targetChild
    }

}

data class ColumnChild(
    val layoutParams: Pair<ColumnState.Child.Params, LinearLayout.LayoutParams>,
    val component: InnerComponent
)

fun createLayoutParams(params: ColumnState.Child.Params) = LinearLayout.LayoutParams(
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
    "fillMax" -> MATCH_PARENT
    "wrapContent" -> WRAP_CONTENT
    // TODO Implement fix size supporting
    else -> throw IllegalArgumentException("Couldn't parse size ")
}