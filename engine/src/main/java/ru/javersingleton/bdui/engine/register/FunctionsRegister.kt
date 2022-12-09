package ru.javersingleton.bdui.engine.register

import ru.javersingleton.bdui.engine.function.Function

class FunctionsRegister : MutableRegister<Function, String> by CommonRegister(
    registerType = "Function",
    accessStrategy = ByTypeStrategy()
)