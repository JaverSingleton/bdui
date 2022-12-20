package ru.javersingleton.bdui.component.box.android_view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import ru.javersingleton.bdui.component.box.state.BoxState
import ru.javersingleton.bdui.render.android_view.*

class BoxComponent(
    private val beduinContext: BeduinViewContext
) : BaseComponent<BoxState, FrameLayout>(), ContainerHelper.LayoutParamsHelper<BoxState.Child> {

    override val type: String = "Box"
    private lateinit var helper: ContainerHelper<BoxState.Child>

    override fun onCreateView(parent: ViewGroup): FrameLayout {
        val view = FrameLayout(parent.context)
        helper = ContainerHelper(
            beduinContext = beduinContext,
            container = view,
            layoutParamsHelper = this,
            viewStorageTag = R.id.box_child_component
        )
        return view
    }

    override fun onBindState(view: FrameLayout, state: BoxState) {
        view.setOnClickListener(
            state.onClick?.let { callback ->
                View.OnClickListener { callback() }
            }
        )
        helper.setItems(
            state.children.value,
            { it.component }
        )
    }

    override fun generateLayoutParams(item: BoxState.Child) =
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
        oldItem: BoxState.Child,
        newItem: BoxState.Child
    ): Boolean = oldItem.params == newItem.params
}

fun parseSizeDp(value: String): Int = when (value) {
    "fillMax" -> ViewGroup.LayoutParams.MATCH_PARENT
    "wrapContent" -> ViewGroup.LayoutParams.WRAP_CONTENT
    // TODO Implement fix size supporting
    else -> throw IllegalArgumentException("Couldn't parse size ")
}