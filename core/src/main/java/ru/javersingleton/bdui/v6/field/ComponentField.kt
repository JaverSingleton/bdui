package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.ComponentLambda
import ru.javersingleton.bdui.v6.Lambda

data class ComponentField(
    override val id: String,
    val componentType: String,
    val params: Field?
) : Field {

    override fun resolve(scope: Lambda.Scope): Field = scope.run {
        val params = params?.resolve(this)
        if (params !is ResolvedField?) {
            return ComponentField(
                id,
                componentType,
                params
            )
        }

        val component: ComponentLambda = remember(id, componentType) {
            inflateComponent(componentType)
        }.value()

        component.setArguments(params?.state?.value<Structure>()?.unbox() ?: mapOf())
        ResolvedField(
            id = id,
            state = component
        )
    }

}