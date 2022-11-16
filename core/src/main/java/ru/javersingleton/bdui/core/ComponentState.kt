package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.ArrayData
import ru.javersingleton.bdui.core.field.ComponentStructure
import ru.javersingleton.bdui.core.field.Primitive
import ru.javersingleton.bdui.core.field.Structure

object ComponentState {

    abstract class Factory<T : Any?> {

        internal fun calculate(
            componentType: String,
            scope: Lambda.Scope,
            args: Structure?
        ): T = Scope(scope, args).create(componentType)

        abstract fun Scope.create(componentType: String): T

        class Scope(
            private val scope: Lambda.Scope,
            val args: Structure?,
        ) : Lambda.Scope by scope {

            fun State<*>.toComponent(): ComponentStructure = value()

            fun <T> State<*>.toLayoutParams(func: Structure.() -> T): T {
                val componentStructure: ComponentStructure = value()
                return componentStructure.params!!.func()
            }

            fun <T> State<*>.toComponentWithParams(
                func: Structure.(component: ComponentStructure) -> T
            ): T {
                val componentStructure: ComponentStructure = value()
                return componentStructure.params!!.func(componentStructure)
            }

            fun <T> State<*>.toObject(
                func: Structure.() -> T
            ): T {
                val structure: Structure = value()
                return structure.func()
            }

            fun State<*>.toStringValue(): String {
                val primitive: Primitive = value()
                return primitive.toString()
            }


            fun State<*>.toInt(): Int {
                val primitive: Primitive = value()
                return primitive.toInt()
            }

            fun <T> State<*>.toArray(mapIndexed: State<*>.(index: Int) -> T): List<T> {
                val array: ArrayData = value()
                val result: MutableList<T> = mutableListOf()
                for (index in 0 until array.size) {
                    result.add(array[index].mapIndexed(index))
                }
                return result.toList()
            }

            fun prop(name: String): State<*> = args?.prop(name)!!

            fun hasProp(name: String): Boolean = args?.hasProp(name) == true

        }

    }

}