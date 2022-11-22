package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.BeduinComponent
import ru.javersingleton.bdui.core.BeduinController
import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.plus

class MainActivity : AppCompatActivity() {
    private val beduin: BeduinController = BeduinController(
        ComponentField(
            type = "Column",
            "children" to ArrayField(
                ComponentField(
                    type = "Column",
                    "children" to ArrayField(
                        ComponentField(
                            type = "Toolbar",
                            "title" to PrimitiveField("Contacts"),
                        ),
                        ComponentField(
                            type = "Text",
                            "text" to PrimitiveField("Contacts"),
                            "textSize" to PrimitiveField("24"),
                            "textAlign" to PrimitiveField("Center"),
                            "layout_width" to PrimitiveField("fillMaxWidth"),
                        ),
                        ComponentField(
                            type = "ContactItem",
                            "avatar" to PrimitiveField("https://www.meme-arsenal.com/memes/25bd41b4371cc7b1c206f98a1619b3cb.jpg"),
                            "name" to PrimitiveField("Happy Cat"),
                            "lastSeen" to PrimitiveField("21.11.2022"),
                        ),
                        ComponentField(
                            type = "ContactItem",
                            "avatar" to PrimitiveField("https://cdn-cbeko.nitrocdn.com/YAysSGytvxeVxHWdRPueSoYmAixjAhdB/assets/static/optimized/rev-a51233b/wp-content/uploads/2021/07/broken-15.jpeg"),
                            "name" to PrimitiveField("Broken Cat"),
                            "lastSeen" to PrimitiveField("20.11.2022"),
                            "indicator" to PrimitiveField(""),
                            id = "contact2"
                        ),
                        ComponentField(
                            type = "Box",
                            "layout_height" to PrimitiveField("fillMaxHeight"),
                            "children" to ArrayField(
                                ComponentField(
                                    type = "Button",
                                    "text" to PrimitiveField("Submit"),
                                    "layout_width" to PrimitiveField("fillMaxWidth"),
                                    "layout_alignment" to PrimitiveField("BottomCenter"),
                                ),
                            )
                        )
                    ),
                )
            ),
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                BeduinComponent(
                    controller = beduin,
                    modifier = Modifier.clickable {
                        beduin.state += ComponentField(
                            type = "ContactItem",
                            "indicator" to PrimitiveField("https://zibuhoker.ru/ifm/indicator.png"),
                            "lastSeen" to PrimitiveField("Online"),
                            id = "contact2"
                        )
                    }.fillMaxWidth()
                )
            }
        }
    }
}