package de.conveyorfight.assets

import com.google.gson.GsonBuilder
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

            val builder = GsonBuilder()
            builder.registerTypeAdapter(Properties::class.java, Properties.Deserializer())
            val gson = builder.create()


            return PropertyValue(
                gson.fromJson(jsonObject, Properties::class.java),
                jsonObject.get("stat").asInt
            )
        }
    }
}
