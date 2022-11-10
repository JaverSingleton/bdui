package ru.javersingleton.bdui.v6

interface State

interface ReadableState : State {

    val currentValue: Any?

    fun subscribe(callback: (State) -> Unit): Subscription

    interface Subscription {

        fun unsubscribe()

    }

}

class ConstState(
    override val currentValue: Any?
): ReadableState {

    override fun subscribe(callback: (State) -> Unit): ReadableState.Subscription = Subscription

    object Subscription: ReadableState.Subscription {
        override fun unsubscribe() { /*Do Nothing*/ }
    }

}

class MutableState(
    defaultValue: Any?
) : ReadableState {
    private val callbacks: MutableSet<(State) -> Unit> = mutableSetOf()

    override var currentValue: Any? = defaultValue
        set(value) {
            if (field != value) {
                field = value
                notifyStateChanged()
            }
        }

    private fun notifyStateChanged() {
        callbacks.forEach { callback ->
            callback(this)
        }
    }

    override fun subscribe(callback: (State) -> Unit): ReadableState.Subscription =
        Subscription(callback)

    inner class Subscription(
        private val callback: (State) -> Unit
    ) : ReadableState.Subscription {

        init {
            callbacks.add(callback)
        }

        override fun unsubscribe() {
            callbacks.remove(callback)
        }

    }

}