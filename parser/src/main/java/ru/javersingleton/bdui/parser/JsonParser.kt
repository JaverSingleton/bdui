package ru.javersingleton.bdui.parser

import org.json.JSONArray
import org.json.JSONObject
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.entity.*
import ru.javersingleton.bdui.engine.meta.MetaComponentBlueprint
import ru.javersingleton.bdui.engine.meta.MetaComponentsStorage
import java.io.Reader

class JsonParser(
    private val componentsCache: MetaComponentsStorage
) : Parser {

    // TODO Узнать может ли быть результат NULL
    override fun parse(reader: Reader): ComponentField {
        val json = reader.readText()
        val obj = JSONObject(json)
        parseMetaComponentsMap(obj.getJSONObject(COMPONENTS), componentsCache)
        return parseComponent(obj.getJSONObject(STATE))!!
    }

    private fun parseMetaComponentsMap(obj: JSONObject, componentsCache: MetaComponentsStorage) {
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
                        refFieldName = strVal.substring(
                            REF_PREFIX.length,
                            strVal.length - REF_SUFFIX.length
                        )
                    )
                } else {
                    PrimitiveField(value = strVal)
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
            ?: parseReference(obj)
            ?: parsePrimitive(obj)
            ?: parseEmpty(obj)
    }

    private fun parseComponent(obj: JSONObject): ComponentField? {
        val fieldType = obj.opt(FIELD_TYPE)
        if (fieldType != null && fieldType != FieldType.COMPONENT) {
            return null
        }
        obj.opt(COMPONENT_TYPE) ?: return null
        var type = ""
        var id: String? = null
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
        val fieldType = obj.opt(FIELD_TYPE)
        if (fieldType != null && fieldType != FieldType.FUNCTION) {
            return null
        }
        obj.opt(FUNCTION_TYPE) ?: return null
        var type = ""
        var id: String? = null
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
        val fieldType = obj.opt(FIELD_TYPE)
        if (fieldType != null && fieldType != FieldType.INTERACTION) {
            return null
        }
        obj.opt(INTERACTION_TYPE) ?: return null
        var type = ""
        var id: String? = null
        val fields = ArrayList<Pair<String, Field<*>>>()
        obj.keys().forEach { key ->
            when (key) {
                INTERACTION_TYPE -> {
                    type = obj.getString(key)
                }
                ID -> id = obj.getString(key)
                else -> fields += key to parseField(obj.getNullable(key))
            }
        }
        return InteractionField(
            id = id,
            interactionType = type,
            params = StructureField(fields = linkedMapOf(*fields.toTypedArray()))
        )
    }

    private fun parseStructure(obj: JSONObject): StructureField? {
        val fieldType = obj.opt(FIELD_TYPE)
        if (fieldType != null && fieldType != FieldType.STRUCTURE) {
            return null
        }
        if (obj.length() == 0 || (obj.length() == 1 && obj.has(ID))) {
            return null
        }
        var id: String? = null
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

    private fun parseReference(obj: JSONObject): ReferenceField? {
        val fieldType = obj.opt(FIELD_TYPE)
        if (fieldType != null && fieldType != FieldType.REFERENCE) {
            return null
        }
        if (obj.length() == 0 || (obj.length() == 1 && obj.has(ID))) {
            return null
        }
        var id: String? = null
        var refFieldName: String? = null
        obj.keys().forEach { key ->
            when (key) {
                ID -> id = obj.getString(key)
                "refFieldName" -> refFieldName = obj.getString(key)
            }
        }
        return ReferenceField(
            id = id,
            refFieldName = refFieldName!!
        )
    }

    private fun parsePrimitive(obj: JSONObject): PrimitiveField? {
        val fieldType = obj.opt(FIELD_TYPE)
        if (fieldType != null && fieldType != FieldType.PRIMITIVE) {
            return null
        }
        if (obj.length() == 0 || (obj.length() == 1 && obj.has(ID))) {
            return null
        }
        var id: String? = null
        var value: String? = null
        obj.keys().forEach { key ->
            when (key) {
                ID -> id = obj.getString(key)
                "value" -> value = obj.getString(key)
            }
        }
        return PrimitiveField(
            id = id,
            value = value!!
        )
    }

    private fun parseEmpty(obj: JSONObject? = null): EmptyField {
        val id: String? = obj?.optString(ID)
        return EmptyField(id)
    }

    private fun JSONObject.getNullable(key: String): Any? = if (isNull(key)) null else get(key)

}

private const val FIELD_TYPE = "fieldType"
enum class FieldType(
    val value: String
) {
    INTERACTION("Interaction"),
    STRUCTURE("Structure"),
    PRIMITIVE("Primitive"),
    COMPONENT("Component"),
    FUNCTION("Function"),
    REFERENCE("Reference");
    // TODO Добавить Array и Empty
}

private const val STATE = "state"
private const val COMPONENTS = "components"
private const val ROOT_COMPONENT = "rootComponent"
private const val COMPONENT_TYPE = "componentType"
private const val FUNCTION_TYPE = "functionType"
private const val INTERACTION_TYPE = "interactionType"
private const val ID = "id"
private const val REF_PREFIX = "{{"
private const val REF_SUFFIX = "}}"
