package ru.javersingleton.bdui.component.meta.state

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.MutableArgumentsStorage
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.asLazyValue
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.entity.*

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

        val componentRoot = prepareComponentRoot(
            scope = this,
            id = args.id,
            componentField = blueprint.rootComponent,
            stateField = blueprint.state.mergeWith(args)
        )
        return MetaState(
            componentRoot.current
                ?: throw IllegalArgumentException("You must use rootComponent for MetaComponent $componentType")
        )
    }

    private fun prepareComponentRoot(
        scope: Scope,
        id: String,
        componentField: ComponentField,
        stateField: StructureField,
    ): Value<ComponentData> = scope.run {
        val args = rememberValue(
            "$id@references",
            componentField.componentType
        ) { MutableArgumentsStorage() }.current!!

        val resolvedParamsField = stateField.resolve(this, args) as ResolvedField<StructureData>
        val resolvedComponentField = (componentField.resolve(this, args) as ResolvedField)

        val stateProps = resolvedParamsField.value.current?.unbox() ?: mapOf()
        val markedProps = resolvedParamsField.dataWithUserId + resolvedComponentField.dataWithUserId

        val selfProp = mapOf(
            "self" to StructureData(
                "id" to PrimitiveData(value = id).asLazyValue()
            ).asLazyValue()
        )

        args.replace(
            scope = this,
            refs = stateProps + markedProps + selfProp
        )

        resolvedComponentField.value
    }

}