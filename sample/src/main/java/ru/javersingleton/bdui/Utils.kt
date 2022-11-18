package ru.javersingleton.bdui


var withNot = false

fun newText(): String {
    val result = if (withNot) {
        "not text"
    } else {
        "text"
    }
    withNot = !withNot
    return result
}