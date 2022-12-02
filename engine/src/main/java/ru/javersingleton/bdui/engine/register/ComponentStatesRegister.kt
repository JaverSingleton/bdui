package ru.javersingleton.bdui.engine.register

import ru.javersingleton.bdui.engine.ComponentStateFactory

class ComponentStatesRegister :
    MutableRegister<ComponentStateFactory<*>> by CommonRegister("ComponentState")