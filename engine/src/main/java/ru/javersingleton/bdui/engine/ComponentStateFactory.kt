package ru.javersingleton.bdui.engine

import android.util.Log
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.*
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

abstract class ComponentStateFactory<T : Any?> : ByTypeStrategy.Element<String> {

    internal fun calculate(
        componentType: String,
        scope: Lambda.Scope,
        args: StructureData?
    ): T {
        Log.d("Beduin", "OnStateCreate: componentType=$componentType")
        return Scope(scope, args).create(componentType)
    }

    abstract fun Scope.create(componentType: String): T

    class Scope(
        private val scope: Lambda.Scope,
        val args: StructureData?,
    ) : Lambda.Scope by scope {

        @Suppress("unused")
        fun Value<*>.asComponent(): ComponentData? = current()

        @Suppress("unused")
        fun <T> Value<*>.asLayoutParams(func: StructureData.() -> T): T? {
            val componentStructure: ComponentData? = current()
            return componentStructure?.params?.func()
        }

        fun <T> Value<*>.asComponentWithParams(
            func: StructureData.(component: ComponentData) -> T
        ): T? {
            val componentStructure: ComponentData? = current()
            return componentStructure?.params?.func(componentStructure)
        }

        fun <T> Value<*>.asObject(
            func: StructureData.() -> T
        ): T? {
            val structure: StructureData? = current()
            return structure?.func()
        }

        fun Value<*>.asString(): String? {
            val primitive: PrimitiveData? = current()
            return primitive?.asString()
        }

        fun Value<*>.asBoolean(): Boolean? {
            val primitive: PrimitiveData? = current()
            return primitive?.asBoolean()
        }

        fun Value<*>.asInt(): Int? {
            val primitive: PrimitiveData? = current()
            return primitive?.asInt()
        }

        fun Value<*>.asInteraction(): ((Map<String, Value<*>>) -> Unit)? {
            val interactionData: InteractionData? = current()
            return interactionData?.let { callback ->
                { args: Map<String, Value<*>> ->
                    sendInteraction(callback.invoke(args))
                }
            }
        }

        fun <T> Value<*>.asList(mapIndexed: Value<*>.(index: Int) -> T): List<T> {
            val array: ArrayData = current()
                ?: return listOf()

            val result: MutableList<T> = mutableListOf()
            for (index in 0 until array.size) {
                result.add(array[index].mapIndexed(index))
            }
            return result.toList()
        }

        fun forEach(func: (key: String) -> Unit) = args?.forEach(func)

        fun prop(name: String): Value<*> = args?.prop(name) ?: Value.NULL

        @Suppress("unused")
        fun hasProp(name: String): Boolean = args?.hasProp(name) == true

    }

}