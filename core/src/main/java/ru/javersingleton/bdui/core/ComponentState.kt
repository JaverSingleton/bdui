package ru.javersingleton.bdui.core

import android.util.Log
import ru.javersingleton.bdui.core.field.*

object ComponentState {

    abstract class Factory<T : Any?> {

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

            fun Value<*>.asComponent(): ComponentData? = current()

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
                return primitive?.toString()
            }

            fun Value<*>.asInt(): Int? {
                val primitive: PrimitiveData? = current()
                return primitive?.toInt()
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

            fun hasProp(name: String): Boolean = args?.hasProp(name) == true

        }

    }

}