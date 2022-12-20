package ru.javersingleton.bdui.render.android_view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import ru.javersingleton.bdui.engine.field.entity.ComponentData

class ContainerHelper<T>(
    private val beduinContext: BeduinViewContext,
    private val container: ViewGroup,
    private val layoutParamsHelper: LayoutParamsHelper<T>,
    private val viewStorageTag: Int,
) {

    fun setItems(
        newItems: List<T>,
        componentData: (T) -> ComponentData,
        key: (T) -> Any = { componentData(it).id },
    ) {
        DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {

                override fun getOldListSize(): Int = container.childCount

                override fun getNewListSize(): Int = newItems.size

                override fun areItemsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val currentChildState = container.getChildAt(oldItemPosition).component.state
                    val targetChildState = newItems[newItemPosition]

                    return key(currentChildState) == key(targetChildState)
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val currentChildState = container.getChildAt(oldItemPosition).component.state
                    val targetChildState = newItems[newItemPosition]

                    return currentChildState == targetChildState
                }

            }
        ).dispatchUpdatesTo(
            object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {
                    for (targetPosition in position until position + count) {
                        val targetChildState = newItems[targetPosition]
                        val targetChildController = InnerComponent(beduinContext)
                        val targetChildView = targetChildController.createOrUpdateView(
                            container,
                            componentData(targetChildState)
                        ).apply {
                            component = ChildComponent(
                                targetChildState,
                                targetChildController
                            )
                        }
                        container.addView(
                            targetChildView,
                            targetPosition,
                            layoutParamsHelper.generateLayoutParams(targetChildState)
                        )
                    }
                }

                override fun onRemoved(position: Int, count: Int) {
                    container.removeViews(position, count)
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    val currentView = container.getChildAt(fromPosition)
                    container.removeViewAt(fromPosition)
                    container.addView(currentView, toPosition)
                }

                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    for (targetPosition in position until position + count) {
                        val currentChildView = container.getChildAt(targetPosition)
                        val currentChildComponent = currentChildView.component
                        val currentChildController = currentChildComponent.controller
                        val currentChildState = currentChildComponent.state

                        val targetChildState = newItems[targetPosition]
                        val targetChildView = currentChildController.createOrUpdateView(
                            parent = container,
                            state = componentData(targetChildState)
                        ).apply {
                            component = ChildComponent(
                                targetChildState,
                                currentChildController
                            )
                        }

                        if (currentChildView != targetChildView) {
                            container.removeViewAt(position)
                            container.addView(
                                targetChildView,
                                position,
                                layoutParamsHelper.generateLayoutParams(targetChildState),
                            )
                        } else if (
                            layoutParamsHelper.areLayoutParamsTheSame(
                                oldItem = currentChildState,
                                newItem = targetChildState
                            )
                        ) {
                            container.layoutParams = layoutParamsHelper.generateLayoutParams(
                                targetChildState
                            )
                        }
                    }
                }

            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    private var View.component: ChildComponent
        get() = getTag(viewStorageTag) as ContainerHelper<T>.ChildComponent
        set(value) {
            setTag(viewStorageTag, value)
        }

    inner class ChildComponent(
        val state: T,
        val controller: InnerComponent
    )

    interface LayoutParamsHelper<T> {

        fun generateLayoutParams(item: T): ViewGroup.LayoutParams

        fun areLayoutParamsTheSame(oldItem: T, newItem: T): Boolean

    }

}