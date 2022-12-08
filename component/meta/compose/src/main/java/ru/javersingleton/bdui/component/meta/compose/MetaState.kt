package ru.javersingleton.bdui.component.meta.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.MutableReferences
import ru.javersingleton.bdui.engine.field.ComponentData
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.StructureData

data class MetaState(
    val childComponent: ComponentData
)

object MetaStateFactory : ComponentStateFactory<MetaState>() {

    override val type: String = "Meta"

    override fun Scope.create(componentType: String): MetaState {
        val blueprint = rememberValue("componentType", componentType) {
            inflateMetaComponentBlueprint(componentType)
        }.current
            ?: throw IllegalArgumentException("MetaComponent $componentType not found")
        val currentParams = blueprint.state.mergeWith(args)

        // TODO Избавиться от Nullable
        val args = rememberValue(
            "${args!!.id}@references",
            componentType
        ) { MutableReferences() }.current!!
        val paramsField = currentParams.resolve(this, args) as ResolvedField<StructureData>
        val paramsData =
            (paramsField.value.current?.unbox() ?: mapOf()) + paramsField.dataWithUserId
        args.replace(this, paramsData)

        val componentField = blueprint.rootComponent.resolve(this, args)
        return MetaState(
            (componentField as ResolvedField).value.current
                ?: throw IllegalArgumentException("You must use rootComponent for MetaComponent $componentType")
        )
    }

}