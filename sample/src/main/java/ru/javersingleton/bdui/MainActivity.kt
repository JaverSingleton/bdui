package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.base.compose.withBase
import ru.javersingleton.bdui.engine.BeduinController
import ru.javersingleton.bdui.engine.interaction.Effect
import ru.javersingleton.bdui.engine.meta.InMemoryComponentsStorage
import ru.javersingleton.bdui.engine.meta.MetaComponentsStorage
import ru.javersingleton.bdui.engine.register.EffectsRegister
import ru.javersingleton.bdui.engine.register.FunctionsRegister
import ru.javersingleton.bdui.function.base.withBase
import ru.javersingleton.bdui.interaction.base.effect.withBase
import ru.javersingleton.bdui.parser.JsonParser
import ru.javersingleton.bdui.render.compose.Beduin
import ru.javersingleton.bdui.render.compose.BeduinComposeContext
import ru.javersingleton.bdui.render.compose.BeduinScope
import ru.javersingleton.bdui.render.compose.ComponentsRegister
import java.io.Reader

class MainActivity : AppCompatActivity() {

    private val metaComponents: MetaComponentsStorage = InMemoryComponentsStorage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reader = asset("sample2.json")
        val parser = JsonParser(metaComponents)

        val context = BeduinComposeContext(
            metaComponents = metaComponents,
            nativeComponents = ComponentsRegister().withBase(),
            functions = FunctionsRegister().withBase(),
            effects = EffectsRegister().withBase()
        )

        val controller = BeduinController(
            context,
            state = parser.parse(reader)
        ) { controller, interaction ->
            when (interaction) {
                is Effect -> controller.state = interaction.run(controller.state)
            }
        }

        setContent {
            Column {
                BeduinScope(context = context) {
                    Beduin(
                        controller = controller,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    private fun asset(name: String): Reader {
        return application.assets.open(name).bufferedReader()
    }

}
