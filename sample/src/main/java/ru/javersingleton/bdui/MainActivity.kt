package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.javersingleton.bdui.component.base.compose.withBase
import ru.javersingleton.bdui.engine.BeduinController
import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.meta.InMemoryComponentsStorage
import ru.javersingleton.bdui.engine.meta.MetaComponentsStorage
import ru.javersingleton.bdui.engine.register.FunctionsRegister
import ru.javersingleton.bdui.function.base.withBase
import ru.javersingleton.bdui.handler.flow.ActionHandler
import ru.javersingleton.bdui.handler.flow.EffectHandler
import ru.javersingleton.bdui.handler.flow.InteractionHandlersRegister
import ru.javersingleton.bdui.interaction.base.effect.withBase
import ru.javersingleton.bdui.parser.JsonParser
import ru.javersingleton.bdui.render.compose.Beduin
import ru.javersingleton.bdui.render.compose.BeduinComposeContext
import ru.javersingleton.bdui.render.compose.BeduinScope
import ru.javersingleton.bdui.render.compose.ComponentsRegister
import java.io.Reader

class MainActivity : AppCompatActivity() {

    private val metaComponents: MetaComponentsStorage = InMemoryComponentsStorage()

    private val interactionHandlers = InteractionHandlersRegister().withBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reader = asset("sample2.json")
        val parser = JsonParser(metaComponents)

        val context = BeduinComposeContext(
            metaComponents = metaComponents,
            nativeComponents = ComponentsRegister().withBase(),
            functions = FunctionsRegister().withBase(),
            effects = interactionHandlers.createInteractionsRegister()
        )

        val controller = BeduinController(
            context,
            state = parser.parse(reader)
        ) { controller, interaction ->
            onInteraction(controller, interaction)
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

    private fun onInteraction(controller: BeduinController, interaction: Interaction) {
        when (val handler = interactionHandlers[interaction]) {
            is EffectHandler -> controller.state = handler.handle(controller.state, interaction)
            is ActionHandler -> lifecycle.coroutineScope.launch {
                handler
                    .handle(controller.state, interaction)
                    .collectLatest { nextInteraction ->
                        onInteraction(controller, nextInteraction)
                    }
            }

        }
    }

    private fun asset(name: String): Reader {
        return application.assets.open(name).bufferedReader()
    }

}
