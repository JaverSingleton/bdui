package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.MutableReferences
import ru.javersingleton.bdui.core.field.ComponentData
import ru.javersingleton.bdui.core.field.ResolvedField
import ru.javersingleton.bdui.core.field.StructureData

object MetaComponent {

    object StateFactory : ComponentState.Factory<MetaState>() {

        override fun Scope.create(componentType: String): MetaState {
            val blueprint = rememberValue("componentType", componentType) {
                inflateMetaComponentBlueprint(componentType)
            }.current
                ?: throw IllegalArgumentException("MetaComponent $componentType not found")
            val currentParams = blueprint.state.mergeWith(args)

            val args = MutableReferences()
            val paramsField = currentParams.resolve(this, args) as ResolvedField<StructureData>
            val paramsData = (paramsField.value.current?.unbox() ?: mapOf()) + paramsField.dataWithUserId
            args.putAll(this, paramsData)

            val componentField = blueprint.rootComponent.resolve(this, args)
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