package ru.javersingleton.bdui.render.android_view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import ru.javersingleton.bdui.engine.BeduinController
import ru.javersingleton.bdui.engine.core.ReadableValue
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.entity.ComponentData

class BeduinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var sub: Subscription? = null

    private lateinit var beduinContext: BeduinViewContext
    private val child: InnerComponent by lazy { InnerComponent(beduinContext) }

    fun setBeduinContext(beduinContext: BeduinViewContext) {
        this.beduinContext = beduinContext
    }

    fun bindController(
        controller: BeduinController
    ) {
        sub?.unsubscribe()
        sub = controller.root.subscribe(
            "Beduin",
            this
        ) { render(it) }
        render(controller.root.currentQuiet!!)
    }

    private fun render(state: ComponentData) {
        replaceIfNeeded(
            index = 0,
            targetView = child.createOrUpdateView(this, state),
            layoutParams = generateDefaultLayoutParams()
        )
    }

}

fun ViewGroup.replaceIfNeeded(
    index: Int,
    targetView: View,
    layoutParams: LayoutParams
) {
    if (index < childCount) {
        val currentView = getChildAt(index)
        if (currentView == targetView) {
            return
        }
        removeViewAt(index)
    }

    addView(targetView, index, layoutParams)
}

fun <T> Value<T>.subscribe(
    name: String,
    view: View,
    callback: (T) -> Unit
): Subscription {
    val readableState = this as ReadableValue<T>
    val listener = object : OnAttachStateChangeListener {
        var bond: ReadableValue.ValidityBond? = null
        override fun onViewAttachedToWindow(v: View?) {
            bond = readableState.bindValidityWith(
                shouldDefer = true,
                child = object : ReadableValue.Invalidatable {
                    override fun onInvalidated(
                        reason: String,
                        postInvalidate: (reason: String, ReadableValue.Invalidatable) -> Unit
                    ) {
                        Log.d("Beduin-Invalidating", "Component $name invalidated by $reason")
                        callback(readableState.currentValue)
                    }
                }
            )
        }

        override fun onViewDetachedFromWindow(v: View?) {
            bond?.unbind()
            bond = null
        }
    }

    return Subscription(view, listener)
}

class Subscription(
    private val view: View,
    private val listener: OnAttachStateChangeListener
) {
    init {
        view.addOnAttachStateChangeListener(listener)
        if (view.isAttachedToWindow) {
            listener.onViewAttachedToWindow(view)
        } else {
            listener.onViewDetachedFromWindow(view)
        }
    }

    fun unsubscribe() {
        view.removeOnAttachStateChangeListener(listener)
    }
}