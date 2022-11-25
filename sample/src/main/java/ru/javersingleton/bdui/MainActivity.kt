package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.BeduinComponent
import ru.javersingleton.bdui.component.interaction.StatePatchEffect
import ru.javersingleton.bdui.core.MemoryComponentsCache
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.interaction.Effect
import ru.javersingleton.bdui.core.plus
import ru.javersingleton.bdui.parser.JsonParser
import java.io.Reader

class MainActivity : AppCompatActivity() {

    private val componentsCache = MemoryComponentsCache()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reader = asset("sample1.json")
        val parser = JsonParser(componentsCache)
        val controller = parser.parse(reader)

        controller.onInteraction = { interaction ->
            when (interaction) {
                is Effect -> controller.state = interaction.run(controller.state)
            }
        }

        setContent {
            Column {
                BeduinComponent(
                    controller = controller,
                    modifier = Modifier
//                        .clickable {
//                            controller.state += asset("sample1_patch1.json").let { reader ->
//                                parser.parseObject(reader)
//                            } as ComponentField
//                        }
                        .fillMaxWidth()
                )
            }
        }
    }

    private fun asset(name: String): Reader {
        return application.assets.open(name).bufferedReader()
    }

}
