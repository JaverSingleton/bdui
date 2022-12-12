package ru.javersingleton.bdui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.javersingleton.bdui.component.base.android_view.withBase
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
import ru.javersingleton.bdui.interaction.delay.flow.ValueCallbackEvent
import ru.javersingleton.bdui.parser.JsonParser
import ru.javersingleton.bdui.render.android_view.BeduinView
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.ComponentsRegister
import java.io.Reader

class MainActivity : AppCompatActivity() {

    private val metaComponents: MetaComponentsStorage = InMemoryComponentsStorage()

    private val parser = JsonParser(metaComponents)
    private val interactionHandlers = InteractionHandlersRegister().withBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reader = asset("sample-short-2.json")
        val parser = JsonParser(metaComponents)

        val context = BeduinViewContext(
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

        setContentView(
            BeduinView(this).apply {
                setBeduinContext(context)
                bindController(controller)
            }
        )
    }

    private fun onInteraction(controller: BeduinController, interaction: Interaction) {
        when (interaction) {
            is ValueCallbackEvent -> {
                controller.state = parser.parse(asset(interaction.value.asString()))
            }
            else -> {
                when (val handler = interactionHandlers[interaction]) {
                    is EffectHandler -> controller.state =
                        handler.handle(controller.state, interaction)
                    is ActionHandler -> lifecycle.coroutineScope.launch {
                        handler
                            .handle(controller.state, interaction)
                            .collectLatest { nextInteraction ->
                                onInteraction(controller, nextInteraction)
                            }
                    }
                }
            }
        }
    }

    private fun asset(name: String): Reader {
        return application.assets.open(name).bufferedReader()
    }

}
