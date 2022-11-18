package ru.javersingleton.bdui.component.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ComponentStructure

@Composable
fun BeduinComponent(
    modifier: Modifier = Modifier,
    root: Value<ComponentStructure>
) {
    val state = root.subscribeAsState()
    BduiComponent(
        modifier = modifier,
        componentStructure = state.value
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