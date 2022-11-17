package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.Structure
import ru.javersingleton.bdui.core.field.resolveThemselves

object MetaComponent {

    object StateFactory : ComponentState.Factory<MetaState>() {

        override fun Scope.create(componentType: String): MetaState {
            val blueprint = rememberValue("componentType", componentType) {
                inflateMetaComponentBlueprint(componentType)
            }.current
            val currentParams = blueprint.state.mergeWith(args)

            val args: Structure = resolveThemselves(
                id = blueprint.state.id,
                params = currentParams
            ).value.current


            val componentField = blueprint.rootComponent.resolve(this, args.unbox())
            return MetaState((componentField as ResolvedField).value.current)
        }

    }

}

data class MetaState(
    val childComponent: ComponentStructure
)