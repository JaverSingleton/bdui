package ru.javersingleton.bdui

import android.os.Bundle
import android.util.JsonReader
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
import ru.javersingleton.bdui.core.field.*
import ru.javersingleton.bdui.core.plus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = assetComponent("sample1.json")
        val controller = BeduinController(root)
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

    private fun assetComponent(name: String): ComponentField {
        val json = readAsset(name)
        val obj = JSONObject(json)
        return parseComponentField(obj) as ComponentField
    }

    private fun readAsset(name: String): String {
        return application.assets.open(name).bufferedReader().run { readText() }
    }

    private fun parseComponentField(value: Any): Field<*> {
        val obj = value as JSONObject
        var type = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                "type" -> type = obj.getString(key)
                "id" -> id = obj.getString(key)
                "children" -> fields += key to ArrayField(
                    fields = parseArrayField(
                        obj.getJSONArray(key),
                        ::parseComponentField
                    )
                )
                else -> fields += key to parseField(obj.get(key))
            }
        }
        return ComponentField(type, fields = fields.toTypedArray(), id = id)
    }

    private fun parseField(value: Any): Field<*> {
        return when (value) {
            is JSONArray -> ArrayField(fields = parseArrayField(value, ::parseField))
            is JSONObject -> StructureField(fields = parseStructureField(value, ::parseField))
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

    private fun parseArrayField(arr: JSONArray, parseFunc: (Any) -> Field<*>): Array<Field<*>> {
        val fields = ArrayList<Field<*>>()
        for (i in 0 until arr.length()) {
            fields += parseFunc(arr[i])
        }
        return fields.toTypedArray()
    }

    private fun parseStructureField(obj: JSONObject, parseFunc: (Any) -> Field<*>): Array<Pair<String, Field<*>>> {
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            fields += key to parseFunc(obj.get(key))
        }
        return fields.toTypedArray()
    }

}