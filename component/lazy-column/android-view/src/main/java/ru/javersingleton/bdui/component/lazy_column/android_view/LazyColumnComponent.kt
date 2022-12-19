package ru.javersingleton.bdui.component.lazy_column.android_view

import android.view.View
import android.view.ViewGroup
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
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val decoder: MutableMap<Int, String> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val componentType = decoder[viewType]!!
        val componentFactory = context.inflateComponentFactory(componentType)
        val emptyComponentData = componentFactory.createEmptyState(context)
        val component = componentFactory.createComponent(context)
        val view = component.createOrUpdateView(parent, emptyComponentData.value)
        view.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        return ViewHolder(
            view,
            component
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.component.updateState(items[position].component.value)
//        holder.view.layoutParams = TODO
        // TODO Добавить LayoutParams
    }

    override fun getItemViewType(position: Int): Int {
        val itemViewType = items[position].component.componentType.hashCode()
        decoder[itemViewType] = items[position].component.componentType
        return itemViewType
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        view: View,
        val component: Component
    ) : RecyclerView.ViewHolder(view)

}