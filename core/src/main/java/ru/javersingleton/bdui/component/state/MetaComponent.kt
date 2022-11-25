package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentData
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.StructureData
import ru.javersingleton.bdui.core.field.resolveThemselves

object MetaComponent {

    object StateFactory : ComponentState.Factory<MetaState>() {

        override fun Scope.create(componentType: String): MetaState {
            val blueprint = rememberValue("componentType", componentType) {
                inflateMetaComponentBlueprint(componentType)
            }.current
                ?: throw IllegalArgumentException("MetaComponent $componentType not found")
            val currentParams = blueprint.state.mergeWith(args)

            val args: StructureData = resolveThemselves(
                id = blueprint.state.id,
                params = currentParams
            ).value.current
                ?: throw UnknownError("Args for $componentType is null")


            val componentField = blueprint.rootComponent.resolve(this, args.unbox())
            return MetaState(
                (componentField as ResolvedField).value.current
                    ?: throw IllegalArgumentException("You must use rootComponent for MetaComponent $componentType")
            )
        }

    }

}

data class MetaState(
    val childComponent: ComponentData
)