package ru.javersingleton.bdui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import org.json.JSONArray
import org.json.JSONObject
import ru.javersingleton.bdui.component.compose.BeduinComponent
import ru.javersingleton.bdui.core.BeduinController
import ru.javersingleton.bdui.core.ComponentsCache
import ru.javersingleton.bdui.core.MemoryComponentsCache
import ru.javersingleton.bdui.core.MetaComponentBlueprint
import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.plus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val componentsCache = MemoryComponentsCache()
        val controller = assetController("sample1.json", componentsCache)
        setContent {
            Column {
                BeduinComponent(
                    controller = controller,
                    modifier = Modifier
                        .clickable {
                            controller.state += assetComponent("sample1_patch1.json")
                        }
                        .fillMaxWidth()
                )
            }
        }
    }

    private fun assetController(name: String, componentsCache: ComponentsCache): BeduinController {
        val json = readAsset(name)
        val obj = JSONObject(json)
        parseMetaComponentsMap(obj.getJSONObject("components"), componentsCache)
        val state = parseComponent(obj.getJSONObject("state"))
        return BeduinController(componentsCache, state)
    }

    private fun assetComponent(name: String): ComponentField {
        val json = readAsset(name)
        val obj = JSONObject(json)
        return parseComponent(obj)
    }

    private fun readAsset(name: String): String {
        return application.assets.open(name).bufferedReader().run { readText() }
    }

    private fun parseMetaComponentsMap(obj: JSONObject, componentsCache: ComponentsCache) {
        val blueprints = ArrayList<Pair<String, MetaComponentBlueprint>>()
        obj.keys().forEach { key ->
            val blueprint = parseMetaComponent(obj.getJSONObject(key))
            blueprints += key to blueprint
            componentsCache.put(key, blueprint)
        }
    }

    private fun parseMetaComponent(obj: JSONObject): MetaComponentBlueprint {
        val state = parseStructure(obj.getJSONObject("state"))
        val rootComponent = parseComponent(obj.getJSONObject("rootComponent"))
        return MetaComponentBlueprint(state, rootComponent)
    }

    private fun parseField(value: Any): Field<*> {
        return when (value) {
            is JSONArray -> parseArray(value)
            is JSONObject -> parseObject(value)
            else -> {
                val strVal = value.toString()
                val isRef = strVal.startsWith("{{").and(strVal.endsWith("}}"))
                if (isRef) {
                    ReferenceField(strVal.substring(2, strVal.length - 2))
                } else {
                    PrimitiveField(strVal)
                }
            }
        }
    }

    private fun parseArray(arr: JSONArray): ArrayField {
        val fields = ArrayList<Field<*>>()
        for (i in 0 until arr.length()) {
            fields += parseField(arr[i])
        }
        return ArrayField(fields = fields.toTypedArray())
    }

    private fun parseObject(obj: JSONObject): Field<*> = when {
        obj.has("componentType") -> parseComponent(obj)
        obj.has("functionType") -> parseFunction(obj)
        else -> parseStructure(obj)
    }

    private fun parseComponent(obj: JSONObject): ComponentField {
        var type = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                "componentType" -> type = obj.getString(key)
                "id" -> id = obj.getString(key)
                else -> fields += key to parseField(obj.get(key))
            }
        }
        return ComponentField(
            id = id,
            componentType = type,
            params = StructureField(*fields.toTypedArray())
        )
    }

    private fun parseFunction(obj: JSONObject): FunctionField {
        var type = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                "functionType" -> type = obj.getString(key)
                "id" -> id = obj.getString(key)
                else -> fields += key to parseField(obj.get(key))
            }
        }
        return FunctionField(
            id = id,
            functionType = type,
            params = StructureField(*fields.toTypedArray())
        )
    }

    private fun parseStructure(obj: JSONObject): StructureField {
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            fields += key to parseField(obj.get(key))
        }
        return StructureField(fields = fields.toTypedArray())
    }

}