package ru.javersingleton.bdui.v3.core.interaction


interface Event {

    interface Factory {

        fun create(factory: InteractionFactory, interaction: Interaction): Event

    }

}



