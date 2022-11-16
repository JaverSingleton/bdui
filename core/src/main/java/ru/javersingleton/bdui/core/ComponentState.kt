package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.ComponentStructure
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

            fun Structure.toComponent(name: String): ComponentStructure {
                TODO()
            }

            fun <T> Structure.toLayoutParams(name: String, func: Structure.() -> T): T {
                TODO()
            }

            fun <T> Structure.toComponentWithParams(
                name: String,
                func: Structure.(ComponentStructure) -> T
            ): T {
                TODO()
            }

            fun <T> Structure.toObject(
                name: String,
                func: Structure.() -> T
            ): T {
                TODO()
            }

            fun Structure.toString(name: String): String {
                TODO()
            }

            fun Structure.toInt(name: String): Int {
                TODO()
            }

            fun Structure.hasProp(name: String): Boolean = args?.contains(name) == true

            fun toComponent(name: String): ComponentStructure {
                TODO()
            }

            fun <T> toLayoutParams(name: String, func: Structure.() -> T): T {
                TODO()
            }

            fun <T> toComponentWithParams(
                name: String,
                func: Structure.(ComponentStructure) -> T
            ): T {
                TODO()
            }

            fun <T> toObject(
                name: String,
                func: Structure.() -> T
            ): T {
                TODO()
            }

            fun toString(name: String): String {
                TODO()
            }

            fun toInt(name: String): Int {
                TODO()
            }

            fun hasProp(name: String): Boolean = args?.contains(name) == true

        }

    }

}