package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
                            type = "ListItem",
                            "title" to PrimitiveField(newText()),
                        ),
                        EmptyField(
                            id = "titleId"
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
                            type = "Text",
                            "text" to PrimitiveField(newText()),
                            id = "titleId"
                        )
                    }
                )
            }
        }
    }
}