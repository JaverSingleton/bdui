package ru.javersingleton.bdui.core.interaction

interface Interaction {
    fun nextStep(): Interaction
}