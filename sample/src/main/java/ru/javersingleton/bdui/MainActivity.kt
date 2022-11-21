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
import ru.javersingleton.bdui.core.field.ArrayField
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.EmptyField
import ru.javersingleton.bdui.core.field.PrimitiveField
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
                            type = "Image",
                            "src" to PrimitiveField("https://www.meme-arsenal.com/memes/80a6a54e64c243b1fd270649a2cdc0f2.jpg"),
                            "contentScale" to PrimitiveField("Fit"),
                            "layout_width" to PrimitiveField("fillMaxWidth"),
                            "layout_height" to PrimitiveField("250"),
                            id = "imageId"
                        ),
                        ComponentField(
                            type = "Text",
                            "text" to PrimitiveField("Hello World!"),
                            "layout_width" to PrimitiveField("wrapContentWidth"),
                            id = "textId",
                        ),
                        ComponentField(
                            type = "Button",
                            "text" to PrimitiveField("Submit"),
                            "layout_width" to PrimitiveField("fillMaxWidth"),
                        ),
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
                            type = "Image",
                            "src" to PrimitiveField("https://www.meme-arsenal.com/memes/46af4bad3574b521c529676373030172.jpg"),
                            id = "imageId"
                        )
                    }.fillMaxWidth()
                )
            }
        }
    }
}