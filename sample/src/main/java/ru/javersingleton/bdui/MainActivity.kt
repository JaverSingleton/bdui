package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.BeduinComponent
import ru.javersingleton.bdui.core.Beduin
import ru.javersingleton.bdui.core.field.ArrayField
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.PrimitiveField

class MainActivity : AppCompatActivity() {
    val beduin: Beduin = Beduin().apply {
        setState(
            ComponentField(
                type = "Column",
                "children" to ArrayField(
                    ComponentField(
                        type = "Column",
                        "children" to ArrayField(
                            ComponentField(
                                type = "Text",
                                "text" to PrimitiveField(newText()),
                                id = "titleId"
                            )
                        ),
                        id = "innerColumnId"
                    )
                ),
                id = "columnId"
            )
        )
    }
    var withNot = false

    fun newText(): String {
        val result = if (withNot) {
            "not text"
        } else {
            "text"
        }
        withNot = !withNot
        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                BeduinComponent(
                    root = beduin.root,
                    modifier = Modifier.clickable {
                        beduin.setState(
                            beduin.currentState.mergeDeeply(
                                "titleId",
                                ComponentField(
                                    type = "Text",
                                    "text" to PrimitiveField(newText())
                                )
                            ) as ComponentField
                        )
                    }
                )
            }
        }
    }
}