package de.conveyorfight.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import de.conveyorfight.R
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class Item(
    val context: Context,
    val round: Int,
    var rarity: Int = -1,
    var itemType: ItemTypes? = null,
    var cost: Int = 5,
    var properties: ArrayList<PropertyValue> = ArrayList<PropertyValue>(),
    var uuid: UUID? = null
) {

    lateinit var bitmap: Bitmap

    init {
        if (properties.isEmpty()) {
            createRandomItem()
        }
        createBitmap()
    }

    private fun createRandomItem() {

        if (rarity == -1) {
            rarity = when (round) {
                1 -> 1
                2 -> determineRarity(80, 100)
                3 -> determineRarity(70, 100)
                4 -> determineRarity(60, 100)
                5 -> determineRarity(50, 90)
                6 -> determineRarity(30, 75)
                7 -> determineRarity(10, 60)
                8 -> determineRarity(0, 45)
                else -> 3
            }
        }

        if (itemType == null) {

            do {
                itemType = ItemTypes.values()[Random().nextInt(ItemTypes.values().size)];
            } while ((itemType == ItemTypes.RangeNull && rarity > 1) ||
                (itemType == ItemTypes.RangeTwo && rarity > 2)
            )
        }

        if (itemType == ItemTypes.Special) {
            if (rarity == 1) {
                properties.add(generateARandomProperty())
            }

        } else {
            generateNonSpecialItem()
        }
    }

    private fun generateARandomProperty(): PropertyValue {
        val property: Properties =
            Properties.values()[Random().nextInt(Properties.values().size)]
        val rarityValue: RarityValue =
            property.detail.rarities.find { value -> value.rarity == 2 }
                ?: return generateARandomProperty()
        return PropertyValue(property, (rarityValue.minValue..rarityValue.maxValue).random())
    }

    private fun generateNonSpecialItem() {
        var numberProperties: Int = if (rarity == 1) {
            (1..2).random()
        } else {
            (2..4).random()
        }

        cost = when (rarity) {
            1 -> 5
            2 -> 15
            else -> 25
        }

        if (rarity == 3) {
            val flashMin = Properties.Flash_Damage.detail.rarities[0].minValue;
            val flashMax = Properties.Flash_Damage.detail.rarities[0].maxValue;
            properties.add(PropertyValue(Properties.Flash_Damage, (flashMin..flashMax).random()))
            numberProperties--
        }


        for (i in 1..numberProperties) {
            val property: Properties =
                Properties.values()[Random().nextInt(Properties.values().size)]
            val rarityValue: RarityValue? =
                property.detail.rarities.find { value -> value.rarity == rarity }
            if (rarityValue == null) {
                numberProperties++
                continue
            }
            if (properties.find { rarityValueOwned -> rarityValueOwned.property == property } != null) {
                numberProperties++
                continue
            }

            properties.add(
                PropertyValue(
                    property,
                    (rarityValue.minValue..rarityValue.maxValue).random()
                )
            )
        }
    }

    private fun determineRarity(percentageRarOne: Int, percentageRarTwo: Int): Int {
        return when ((0..100).random()) {
            in 0..percentageRarOne -> 1
            in percentageRarOne + 1..percentageRarTwo -> 2
            else -> 3
        }
    }

    fun createBitmap() {
        bitmap = when (ItemTypeRarity(itemType!!, rarity)) {
            ItemTypeRarity(ItemTypes.Helmet, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.leather_helm)
            ItemTypeRarity(ItemTypes.Helmet, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.metal_helmet)
            ItemTypeRarity(ItemTypes.Helmet, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.epic_helmet)
            ItemTypeRarity(ItemTypes.Gloves, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.leather_gloves)
            ItemTypeRarity(ItemTypes.Gloves, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.metal_gloves)
            ItemTypeRarity(ItemTypes.Gloves, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.epic_gloves)
            ItemTypeRarity(ItemTypes.Armor, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.leather_armor)
            ItemTypeRarity(ItemTypes.Armor, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.metal_armor)
            ItemTypeRarity(ItemTypes.Armor, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.epic_armor)
            ItemTypeRarity(ItemTypes.Pants, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.leather_pants)
            ItemTypeRarity(ItemTypes.Pants, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.metal_pants)
            ItemTypeRarity(ItemTypes.Pants, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.epic_pants)
            ItemTypeRarity(ItemTypes.Shoes, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.leather_boots)
            ItemTypeRarity(ItemTypes.Shoes, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.metal_shoes)
            ItemTypeRarity(ItemTypes.Shoes, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.epic_shoes)
            ItemTypeRarity(ItemTypes.Special, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.potion)
            ItemTypeRarity(ItemTypes.Special, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.magic_wand)
            ItemTypeRarity(ItemTypes.Special, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.shield)
            ItemTypeRarity(ItemTypes.RangeNull, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.knuckle_duster)
            ItemTypeRarity(ItemTypes.RangeTwo, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.dagger)
            ItemTypeRarity(ItemTypes.RangeTwo, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.hammer)
            ItemTypeRarity(ItemTypes.RangeFour, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.club)
            ItemTypeRarity(ItemTypes.RangeFour, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.axe)
            ItemTypeRarity(ItemTypes.RangeFour, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.great_sword)
            ItemTypeRarity(ItemTypes.RangeSix, 1) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.monkey_staff)
            ItemTypeRarity(ItemTypes.RangeSix, 2) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.spear)
            ItemTypeRarity(ItemTypes.RangeSix, 3) ->
                BitmapFactory.decodeResource(context.resources, R.drawable.scythe)
            else -> return
        }
    }

    fun clone(): Item {
        val item = Item(
            context, round, rarity, itemType, cost,
            properties.clone() as ArrayList<PropertyValue>, uuid
        )
        item.createBitmap()
        return item
    }

    override fun toString(): String {
        return "Item(uuid=$uuid, round=$round, rarity=$rarity, itemType=$itemType, cost=$cost, properties=$properties)"
    }

    class Deserializer(private val globalContext: Context) : JsonDeserializer<Item> {

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Item {
            val jsonObject = json!!.asJsonObject
            println("deserialization started")
            println(jsonObject)


            println(jsonObject.get("itemTyp").asString)
            val typ = ItemTypes.valueOfCaseInsensitive(jsonObject.get("itemTyp").asString)
            println(typ)

            //TODO rangetyp

            val gsonFac = GsonBuilder()
            gsonFac.registerTypeAdapter(PropertyValue::class.java, PropertyValue.Deserializer())
            val gson = gsonFac.create()

            //props
            val properties = ArrayList<PropertyValue>()
            for (j: JsonElement in jsonObject.get("properties").asJsonArray.asIterable()) {
                properties.add(gson.fromJson(j, PropertyValue::class.java))
                println(properties.last())
            }

            return Item(
                this.globalContext,
                jsonObject.get("round").asInt,
                jsonObject.get("rarity").asInt,
                typ,
                jsonObject.get("cost").asInt,
                properties,
                gson.fromJson(jsonObject.get("uuid"), UUID::class.java)
            );
        }
    }
}