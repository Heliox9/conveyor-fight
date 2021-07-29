package de.conveyorfight.assets

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class PropertyValue(val property: Properties, var value: Int) {
    public class Deserializer : JsonDeserializer<PropertyValue> {

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): PropertyValue {
            val jsonObject = json!!.asJsonObject
            println(json)

            var element = jsonObject.get("element").asString
            element = element.substring(0, 1) + element.substring(1)
                .lowercase() + "_" + (if (jsonObject.get("typ").asBoolean) "Damage" else "Armor")
            println(element)

            var propertie: Properties = Properties.valueOf(element)
            println(propertie)

            println("stat: ${jsonObject.get("stat")}")

            return PropertyValue(propertie, jsonObject.get("stat").asInt)
            TODO("Not yet implemented")
        }
    }
}
