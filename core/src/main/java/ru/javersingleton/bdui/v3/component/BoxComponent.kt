package ru.javersingleton.bdui.v3.component

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import ru.javersingleton.bdui.v3.*


class BoxComponent : ViewComponent() {

    override fun onBindView(state: State) {
        // TODO Поддержать переинфлейтинг детей при смене состояния
    }

    override fun onCreateView(context: Context, emptyState: State): View =
        FrameLayout(context).apply {
            addComponent(emptyState.getComponentBlueprint("child"))
        }

    private fun FrameLayout.addComponent(blueprint: ComponentBlueprint?) {
        if (blueprint == null) {
            return
        }

        addView(inflateChild(blueprint).view, generateLayoutParams(blueprint.statePatch))
    }

    private fun generateLayoutParams(statePatch: StatePatch): FrameLayout.LayoutParams =
        FrameLayout.LayoutParams(
            statePatch.getInt("width") ?: ViewGroup.LayoutParams.MATCH_PARENT,
            statePatch.getInt("height") ?: ViewGroup.LayoutParams.MATCH_PARENT,
            statePatch.getInt("gravity") ?: (Gravity.TOP or Gravity.START)
        )

}