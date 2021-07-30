package de.conveyorfight.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.*
import de.conveyorfight.R
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class Item(
    private val context: Context,
    val round: Int,
    var rarity: Int = -1,
    var itemType: ItemTypes? = null,
    var cost: Int = 5,
    var properties: ArrayList<PropertyValue> = ArrayList<PropertyValue>(),
    var uuid: UUID? = null
) {

    public fun getContext(): Context {
        return context
    }

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
                itemType = ItemTypes.values()[Random().nextInt(ItemTypes.values().size)]
            } while ((itemType == ItemTypes.RangeNull && rarity > 1) ||
                (itemType == ItemTypes.RangeTwo && rarity > 2)
            )
        }

        if (itemType == ItemTypes.Special) {
            if (rarity == 1) {
                properties.add(generateARandomProperty())
            }
            determineCost()
        } else { generateNonSpecialItem() }
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

        determineCost()

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

    private fun determineCost() {
        cost = when (rarity) {
            1 -> 5
            2 -> 15
            else -> 25
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
            val typString = jsonObject.get("itemTyp").asString
            var typ: ItemTypes = if (typString != "WEAPON") {
                ItemTypes.valueOfCaseInsensitive(typString)!!
            } else {
                // range for weapons
                when (val range = jsonObject.get("range").asInt) {
                    0 -> ItemTypes.RangeNull
                    2 -> ItemTypes.RangeTwo
                    4 -> ItemTypes.RangeFour
                    6 -> ItemTypes.RangeSix
                    else -> throw IllegalArgumentException("$range is not a valid range value")
                }
            }


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

    class Serializer() : JsonSerializer<Item> {
        /**
         * Gson invokes this call-back method during serialization when it encounters a field of the
         * specified type.
         *
         *
         * In the implementation of this call-back method, you should consider invoking
         * [JsonSerializationContext.serialize] method to create JsonElements for any
         * non-trivial field of the `src` object. However, you should never invoke it on the
         * `src` object itself since that will cause an infinite loop (Gson will call your
         * call-back method again).
         *
         * @param src the object that needs to be converted to Json.
         * @param typeOfSrc the actual type (fully genericized version) of the source object.
         * @return a JsonElement corresponding to the specified object.
         */
        override fun serialize(
            src: Item?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            val gson = Gson()

            val merchant = JsonObject()

            if (src != null) {
                merchant.add("uuid", gson.toJsonTree(src.uuid))
            }

            return merchant
            TODO("Not yet implemented")
        }

    }
}