package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.Beduin
import ru.javersingleton.bdui.core.field.ArrayField
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.PrimitiveField

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                val field = remember {
                    mutableStateOf(
                        ComponentField(
                            type = "Column",
                            "children" to ArrayField(
                                ComponentField(
                                    "Text",
                                    "text" to PrimitiveField("Meta"),
                                    "textSize" to PrimitiveField("25"),
                                    "layout_width" to PrimitiveField("20"),
                                    id = "meta_title"
                                ),
                                ComponentField(
                                    "ListItem",
                                    "title" to PrimitiveField("Title"),
                                    "subtitle" to PrimitiveField("Subtitle"),
                                    "layout_width" to PrimitiveField("100"),
                                    "footer" to ComponentField(
                                        "ListItem",
                                        "title" to PrimitiveField("Title 2"),
                                        "subtitle" to PrimitiveField("Subtitle 2"),
                                        "layout_width" to PrimitiveField("100"),
                                    )
                                )
                            )
                        )
                    )
                }
                Beduin(
                    state = field.value,
                    modifier = Modifier.clickable {
                        val value = field.value
                        val newField = value.mergeDeeply(
                            "meta_title",
                            ComponentField(
                                "Text",
                                "text" to PrimitiveField("Meta2"),
                                "textSize" to PrimitiveField("100"),
                                "layout_width" to PrimitiveField("fillMaxWidth")
                            )
                        ) as ComponentField
                        field.value = newField
                    }
                )
            }
        }
    }
}