package de.conveyorfight.assets

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

enum class Properties(val detail: PropertyDetail) {
    Physical_Armor(
        PropertyDetail(
            false,
            null,
            listOf(RarityValue(1, 1, 8), RarityValue(2, 9, 15))
        )
    ),
    Physical_Damage(
        PropertyDetail(
            true,
            Physical_Armor,
            listOf(RarityValue(1, 5, 10), RarityValue(2, 11, 20))
        )
    ),
    Earth_Armor(PropertyDetail(false, null, listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Earth_Damage(
        PropertyDetail(
            true,
            Earth_Armor,
            listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35))
        )
    ),
    Air_Armor(PropertyDetail(false, null, listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Air_Damage(
        PropertyDetail(
            true,
            Air_Armor,
            listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35))
        )
    ),
    Water_Armor(PropertyDetail(false, null, listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Water_Damage(
        PropertyDetail(
            true,
            Water_Armor,
            listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35))
        )
    ),
    Fire_Armor(PropertyDetail(false, null, listOf(RarityValue(2, 9, 15), RarityValue(3, 16, 21)))),
    Fire_Damage(
        PropertyDetail(
            true,
            Fire_Armor,
            listOf(RarityValue(2, 11, 20), RarityValue(3, 21, 35))
        )
    ),
    Flash_Damage(PropertyDetail(true, null, listOf(RarityValue(3, 15, 20))));

    class Deserializer : JsonDeserializer<Properties> {

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Properties {
            val jsonObject = json!!.asJsonObject
            println("Properties+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
            println(json)

            var element = jsonObject.get("element").asString
            element = element.substring(0, 1) + element.substring(1)
                .lowercase() + "_" + (if (jsonObject.get("typ").asBoolean) "Damage" else "Armor")
            println(element)

            var propertie: Properties = valueOf(element)
            println("Deserialized Propertie: $propertie")
            return propertie
        }

    }
}