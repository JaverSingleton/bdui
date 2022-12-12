package ru.javersingleton.bdui.component.button.android_view

import ru.javersingleton.bdui.component.button.state.ButtonState
import ru.javersingleton.bdui.component.button.state.ButtonStateFactory
import ru.javersingleton.bdui.render.android_view.Component
import ru.javersingleton.bdui.render.android_view.ComponentFactory

object ButtonComponentFactory: ComponentFactory<ButtonState>(ButtonStateFactory) {

    override val type: String
        get() = "Button"

    override fun createComponent(): Component = ButtonComponent()

}