package ru.javersingleton.bdui.component.base.android_view

import ru.javersingleton.bdui.component.button.android_view.ButtonComponentFactory
import ru.javersingleton.bdui.render.android_view.ComponentsRegister

fun ComponentsRegister.withBase(): ComponentsRegister = apply {
    register(
        ButtonComponentFactory
    )
}