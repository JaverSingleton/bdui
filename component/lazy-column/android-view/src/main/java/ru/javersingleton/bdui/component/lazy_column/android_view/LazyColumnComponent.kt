package ru.javersingleton.bdui.component.lazy_column.android_view

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.javersingleton.bdui.component.lazy_column.state.LazyColumnState
import ru.javersingleton.bdui.render.android_view.BaseComponent
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.Component

class LazyColumnComponent(
    beduinContext: BeduinViewContext
) : BaseComponent<LazyColumnState, RecyclerView>() {

    override val type: String = "LazyColumn"

    private val adapter = LazyColumnAdapter(beduinContext)

    override fun onCreateView(parent: ViewGroup): RecyclerView =
        RecyclerView(parent.context).apply {
            layoutManager = LinearLayoutManager(parent.context)
            adapter = this@LazyColumnComponent.adapter
        }

    override fun onBindState(view: RecyclerView, state: LazyColumnState) {
        adapter.items = state.children.value
    }

}

class LazyColumnAdapter(
    private val context: BeduinViewContext
) : RecyclerView.Adapter<LazyColumnAdapter.ViewHolder>() {

    var items: List<LazyColumnState.Child> = listOf()
        set(newItems) {
            val oldItems = field
            field = newItems
            calculateDiff(
                oldItems,
                newItems,
                areItemsTheSame = { oldItem, newItem ->
                    oldItem.component.id == newItem.component.id
                },
                areContentsTheSame = { oldItem, newItem ->
                    oldItem == newItem
                }
            ).dispatchUpdatesTo(this)
        }

    private val decoder: MutableMap<Int, String> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val componentType = decoder[viewType]!!
        val componentFactory = context.inflateComponentFactory(componentType)
        val emptyComponentData = componentFactory.createEmptyState(context)
        val component = componentFactory.createComponent(context)
        val view = component.createOrUpdateView(parent, emptyComponentData.value)
        view.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT,
        )
        return ViewHolder(
            view,
            component
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val child = items[position]
        holder.component.updateState(child.component.value)
        if (holder.state?.params != child.params) {
            holder.view.layoutParams = createLayoutParams(child.params)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemViewType = items[position].component.componentType.hashCode()
        decoder[itemViewType] = items[position].component.componentType
        return itemViewType
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        val view: View,
        val component: Component,
        val state: LazyColumnState.Child? = null,
    ) : RecyclerView.ViewHolder(view)

    // TODO Вынести в общие модули
    fun createLayoutParams(params: LazyColumnState.Child.Params) = MarginLayoutParams(
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
        "fillMax" -> LayoutParams.MATCH_PARENT
        "wrapContent" -> LayoutParams.WRAP_CONTENT
        // TODO Implement fix size supporting
        else -> throw IllegalArgumentException("Couldn't parse size ")
    }

}

fun <T> calculateDiff(
    oldItems: List<T>,
    newItems: List<T>,
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
): DiffResult = DiffUtil.calculateDiff(
    object : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean = areItemsTheSame(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean = areContentsTheSame(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )

    }
)