package ru.javersingleton.bdui.component.base.android_view

import ru.javersingleton.bdui.component.box.android_view.BoxComponentFactory
import ru.javersingleton.bdui.component.button.android_view.ButtonComponentFactory
import ru.javersingleton.bdui.component.column.compose.ColumnComponentFactory
import ru.javersingleton.bdui.component.meta.android_view.MetaComponentFactory
import ru.javersingleton.bdui.render.android_view.ComponentsRegister

fun ComponentsRegister.withBase(): ComponentsRegister = apply {
    val metaComponentFactory = MetaComponentFactory
    register(
        ButtonComponentFactory,
        ColumnComponentFactory,
        BoxComponentFactory,
    ) { metaComponentFactory }
}