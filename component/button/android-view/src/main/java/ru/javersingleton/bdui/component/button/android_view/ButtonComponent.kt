package ru.javersingleton.bdui.component.button.android_view

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ru.javersingleton.bdui.component.button.state.ButtonState
import ru.javersingleton.bdui.render.android_view.BaseComponent

class ButtonComponent : BaseComponent<ButtonState, Button>() {
    override val type: String = "Button"

    override fun onCreateView(parent: ViewGroup): Button = Button(parent.context)

    override fun onBindState(view: Button, state: ButtonState) {
        with(view) {
            text = state.text
            setOnClickListener(
                state.onClick?.let { callback ->
                    View.OnClickListener { callback() }
                }
            )
        }
    }

}