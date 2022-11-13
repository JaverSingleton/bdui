package ru.javersingleton.bdui.v6

interface Component {

    abstract class StateFactory<T: Any> {

        fun calculate(scope: ComponentLambda.Scope): T = scope.getState()

        abstract fun ComponentLambda.Scope.getState(): T

    }

    class Factory(
        private val context: BeduinContext,
        private val componentType: String,
    ): StateFactory<List<Any>>() {

        override fun Lambda.Scope.getState(): List<Any> {
            val defaultFields = remember("componentTypeDefaultFields", componentType) {
                context.inflateComponentFields(componentType)
            }

            listOf(
                newComponent("component").value(),
                toTyped(argument("input")) {
                    "innerInput" to structureField("innerInput").value<Any>()
                }
            )
        }

    }

}