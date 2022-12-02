package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.register.FunctionsRegister

fun FunctionsRegister.withBase(): FunctionsRegister = apply {
    register(
        CheckEqualsFunction,
        CheckNullFunction,
        CombineArraysFunction,
        ConditionFunction,
        JoinToStringFunction,
        MaxLengthFunction,
        NotFunction
    )
}