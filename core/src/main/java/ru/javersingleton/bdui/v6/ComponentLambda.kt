package ru.javersingleton.bdui.v6

class ComponentLambda(
    private val componentType: String,
    private val context: BeduinContext,
    private val stateFactory: Component.StateFactory<*>
) : Lambda() {

    private val defaultArgs: Map<String, Any> by lazy {
        context.inflateComponentFields(componentType)
    }

    override fun Lambda.Call.invoke(): Any = Call(this).invoke {
        stateFactory.calculate(this)
    }

    inner class Call(
        private val fields:
        private val call: Lambda.Call
    ) : Scope, Lambda.Scope by call {

        fun invoke(func: Scope.() -> Any): Any = call.invoke {
            func()
        }

        @Suppress("UNCHECKED_CAST")
        override fun <O> getArgumentState(name: String): O =
            when {
                defaultArgs.containsKey(name) -> defaultArgs[name] as O
                else -> call.getArgumentState(name)
            }

    }

    interface Scope : Lambda.Scope {

    }

}