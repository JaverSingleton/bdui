package ru.javersingleton.bdui.parser

import org.json.JSONArray
import org.json.JSONObject
import ru.javersingleton.bdui.core.BeduinController
import ru.javersingleton.bdui.core.ComponentsCache
import ru.javersingleton.bdui.core.MainBeduinContext
import ru.javersingleton.bdui.core.MetaComponentBlueprint
import ru.javersingleton.bdui.core.field.*
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

    override fun parseObject(reader: Reader): Field<*> {
        val json = reader.readText()
        val obj = JSONObject(json)
        return parseObject(obj)
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
            ?: throw IllegalArgumentException("state must be provided")
        val rootComponent = parseComponent(obj.getJSONObject(ROOT_COMPONENT))
            ?: throw IllegalArgumentException("rootComponent must be provided")
        return MetaComponentBlueprint(state, rootComponent)
    }

    private fun parseField(value: Any?): Field<*> {
        return when (value) {
            is JSONArray -> parseArray(value)
            is JSONObject -> parseObject(value)
            null -> parseEmpty()
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
        return ArrayField(fields = fields)
    }

    private fun parseObject(obj: JSONObject): Field<*> {
        return parseComponent(obj)
            ?: parseInteraction(obj)
            ?: parseFunction(obj)
            ?: parseStructure(obj)
            ?: parseEmpty(obj)
    }

    private fun parseComponent(obj: JSONObject): ComponentField? {
        obj.opt(COMPONENT_TYPE) ?: return null
        var type = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                COMPONENT_TYPE -> type = obj.getString(key)
                ID -> id = obj.getString(key)
                else -> fields += key to parseField(obj.getNullable(key))
            }
        }
        return ComponentField(
            id = id,
            componentType = type,
            params = StructureField(fields = linkedMapOf(*fields.toTypedArray()))
        )
    }

    private fun parseFunction(obj: JSONObject): FunctionField? {
        obj.opt(FUNCTION_TYPE) ?: return null
        var type = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                FUNCTION_TYPE -> type = obj.getString(key)
                ID -> id = obj.getString(key)
                else -> fields += key to parseField(obj.getNullable(key))
            }
        }
        return FunctionField(
            id = id,
            functionType = type,
            params = StructureField(fields = linkedMapOf(*fields.toTypedArray()))
        )
    }

    private fun parseInteraction(obj: JSONObject): InteractionField? {
        obj.opt(EFFECT_TYPE) ?: return null
        var type = ""
        var name = ""
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                EFFECT_TYPE -> {
                    type = "effect"
                    name = obj.getString(key)
                }
                ID -> id = obj.getString(key)
                else -> fields += key to parseField(obj.getNullable(key))
            }
        }
        return InteractionField(
            id = id,
            interactionType = type,
            interactionName = name,
            params = StructureField(fields = linkedMapOf(*fields.toTypedArray()))
        )
    }

    private fun parseStructure(obj: JSONObject): StructureField? {
        if (obj.length() == 0 || (obj.length() == 1 && obj.has(ID))) {
            return null
        }
        var id: String = newId()
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                ID -> id = obj.getString(key)
                else -> fields += key to parseField(obj.getNullable(key))
            }
        }
        return StructureField(
            id = id,
            fields = linkedMapOf(*fields.toTypedArray())
        )
    }

    private fun parseEmpty(obj: JSONObject? = null): EmptyField {
        val id: String = obj?.optString(ID) ?: newId()
        return EmptyField(id)
    }

    private fun JSONObject.getNullable(key: String): Any? = if (isNull(key)) null else get(key)

}

private const val STATE = "state"
private const val COMPONENTS = "components"
private const val ROOT_COMPONENT = "rootComponent"
private const val COMPONENT_TYPE = "componentType"
private const val FUNCTION_TYPE = "functionType"
private const val EFFECT_TYPE = "effectType"
private const val ID = "id"
private const val REF_PREFIX = "{{"
private const val REF_SUFFIX = "}}"
