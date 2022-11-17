package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.Structure
import ru.javersingleton.bdui.core.field.resolveThemselves

object MetaComponent {

    class StateFactory : ComponentState.Factory<MetaState>() {

        override fun Scope.create(componentType: String): MetaState {
            val defaultParams = inflateDefaultFields(componentType)
            val currentParams = defaultParams.mergeWith(args)

            val args: Structure? = resolveThemselves(
                id = defaultParams.id,
                params = currentParams
            ).state.value()

            val rootComponent = inflateRootComponent(componentType)
            val componentField = rootComponent.resolve(this, args?.unbox() ?: mapOf())
            return MetaState((componentField as ResolvedField).state.value())
        }

    }

}

data class MetaState(
    val childComponent: ComponentStructure
)