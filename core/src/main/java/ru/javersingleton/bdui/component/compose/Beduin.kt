package ru.javersingleton.bdui.component.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.MainBeduinContext
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.ComponentStructure
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.resolveThemselves

@Composable
fun Beduin(
    modifier: Modifier = Modifier,
    state: ComponentField
) {
    // TODO Провести сюда контекст
    val lambda = remember { Lambda(MainBeduinContext()) }
    val componentStructure = remember(key1 = state, lambda) {
        lambda.setBody {
            val componentField = ComponentField(
                id = state.id,
                componentType = state.componentType,
                params = resolveThemselves("${state.id}@params", state.params)
            )
            val processedField = componentField.resolve(this, mutableMapOf())
            if (processedField !is ResolvedField) {
                throw IllegalArgumentException()
            }

            processedField.value.current<ComponentStructure>()
        }
        lambda.currentValue as ComponentStructure
    }
    BduiComponent(
        modifier = modifier,
        componentStructure = componentStructure
    )
}

/*
{
  "components": {
    "DemoScreen": {
      "state": {
        "title": ""
      },
      "rootComponent": {
        "componentType": "Text",
        "statePatch": {
          "text": "{{title}}"
        }
      }
    }
  },
  "state": {
    "componentType": "DemoScreen",
    "statePatch": {
      "title": "Demo Title"
    }
  }
}
 */