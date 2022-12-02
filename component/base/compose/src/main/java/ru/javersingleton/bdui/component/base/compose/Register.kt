package ru.javersingleton.bdui.component.base.compose

import ru.javersingleton.bdui.component.box.compose.BoxComponent
import ru.javersingleton.bdui.component.button.compose.ButtonComponent
import ru.javersingleton.bdui.component.column.compose.ColumnComponent
import ru.javersingleton.bdui.component.image.compose.ImageComponent
import ru.javersingleton.bdui.component.input.compose.InputComponent
import ru.javersingleton.bdui.component.meta.compose.MetaComponent
import ru.javersingleton.bdui.component.row.compose.RowComponent
import ru.javersingleton.bdui.component.switch.compose.SwitchComponent
import ru.javersingleton.bdui.component.text.compose.TextComponent
import ru.javersingleton.bdui.component.toolbar.compose.ToolbarComponent
import ru.javersingleton.bdui.render.compose.ComponentsRegister

fun ComponentsRegister.withBase(): ComponentsRegister = apply {
    register(
        BoxComponent,
        ColumnComponent,
        ButtonComponent,
        ImageComponent,
        InputComponent,
        RowComponent,
        SwitchComponent,
        TextComponent,
        ToolbarComponent,
    ) { MetaComponent }
}