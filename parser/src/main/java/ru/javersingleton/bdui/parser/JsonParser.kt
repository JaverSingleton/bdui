package ru.javersingleton.bdui.parser

import org.json.JSONArray
import org.json.JSONObject
import ru.javersingleton.bdui.core.BeduinController
import ru.javersingleton.bdui.core.ComponentsCache
import ru.javersingleton.bdui.core.MainBeduinContext
import ru.javersingleton.bdui.core.MetaComponentBlueprint
import ru.javersingleton.bdui.core.field.ArrayField
import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.Field
import ru.javersingleton.bdui.core.field.FunctionField
import ru.javersingleton.bdui.core.field.PrimitiveField
import ru.javersingleton.bdui.core.field.ReferenceField
import ru.javersingleton.bdui.core.field.StructureField
import ru.javersingleton.bdui.core.field.newId
import java.io.Reader

class JsonParser(
    private val componentsCache: ComponentsCache
) : Parser {

    override fun parse(reader: Reader): BeduinController {
        val json = reader.readText()
        val obj = JSONObject(json)
        parseMetaComponentsMap(obj.getJSONObject(COMPONENTS), componentsCache)
        val state = parseComponent(obj.getJSONObject(STATE))
        val context = MainBeduinContext(componentsCache)
        return BeduinController(context, state)
    }

    override fun parseComponent(reader: Reader): ComponentField {
        val json = reader.readText()
        val obj = JSONObject(json)
        return parseComponent(obj)
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
        val state = parseStructure(obj.getJSONObject(STATE))
        val rootComponent = parseComponent(obj.getJSONObject(ROOT_COMPONENT))
        return MetaComponentBlueprint(state, rootComponent)
    }

    private fun parseField(value: Any): Field<*> {
        return when (value) {
            is JSONArray -> parseArray(value)
            is JSONObject -> parseObject(value)
            else -> {
                val strVal = value.toString()
                val isRef = strVal.startsWith(REF_PREFIX).and(strVal.endsWith(REF_SUFFIX))
                if (isRef) {
                    ReferenceField(
                        strVal.substring(
                            REF_PREFIX.length,
                            strVal.length - REF_SUFFIX.length
                        )
                    )
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
        obj.has(COMPONENT_TYPE) -> parseComponent(obj)
        obj.has(FUNCTION_TYPE) -> parseFunction(obj)
        else -> parseStructure(obj)
    }

    private fun parseComponent(obj: JSONObject): ComponentField {
        var type = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                COMPONENT_TYPE -> type = obj.getString(key)
                ID -> id = obj.getString(key)
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
                FUNCTION_TYPE -> type = obj.getString(key)
                ID -> id = obj.getString(key)
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

private const val STATE = "state"
private const val COMPONENTS = "components"
private const val ROOT_COMPONENT = "rootComponent"
private const val COMPONENT_TYPE = "componentType"
private const val FUNCTION_TYPE = "functionType"
private const val ID = "id"
private const val REF_PREFIX = "{{"
private const val REF_SUFFIX = "}}"
