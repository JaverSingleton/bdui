package ru.javersingleton.bdui.component.column.compose

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import ru.javersingleton.bdui.component.column.android_view.R
import ru.javersingleton.bdui.component.column.state.ColumnState
import ru.javersingleton.bdui.render.android_view.BaseComponent
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.ContainerHelper

class ColumnComponent(
    private val beduinContext: BeduinViewContext
) : BaseComponent<ColumnState, LinearLayout>(),
    ContainerHelper.LayoutParamsHelper<ColumnState.Child> {

    override val type: String = "Column"
    private lateinit var helper: ContainerHelper<ColumnState.Child>

    override fun onCreateView(parent: ViewGroup): LinearLayout{
        val view = LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
        }

        helper = ContainerHelper(
            beduinContext = beduinContext,
            container = view,
            layoutParamsHelper = this@ColumnComponent,
            viewStorageTag = R.id.column_child_component
        )

        return view
    }

    override fun onBindState(view: LinearLayout, state: ColumnState) {
        helper.setItems(
            newItems = state.children.value,
            componentData = { item -> item.component }
        )
    }

    override fun generateLayoutParams(item: ColumnState.Child): ViewGroup.LayoutParams =
        with(item.params) {
            LinearLayout.LayoutParams(
                parseSizeDp(width),
                parseSizeDp(height)
            ).apply {
                setMargins(
                    padding?.start ?: 0,
                    padding?.top ?: 0,
                    padding?.end ?: 0,
                    padding?.bottom ?: 0,
                )
            }
        }

    override fun areLayoutParamsTheSame(
        oldItem: ColumnState.Child,
        newItem: ColumnState.Child
    ): Boolean = oldItem.params == newItem.params

}

fun parseSizeDp(value: String): Int = when (value) {
    "fillMax" -> MATCH_PARENT
    "wrapContent" -> WRAP_CONTENT
    // TODO Implement fix size supporting
    else -> throw IllegalArgumentException("Couldn't parse size ")
}