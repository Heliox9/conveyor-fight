package de.conveyorfight.assets

import android.content.Context
import java.util.*

class Item(val context: Context,
           val round: Int,
           var rarity: Int = -1,
           var itemType:ItemTypes? = null,
           var cost: Int = 5,
           var properties: ArrayList<PropertyValue> = ArrayList<PropertyValue>()) {



    init {
        if (properties.isEmpty()){
            createRandomItem()
        }
        createBitmap()
    }

    private fun createRandomItem() {

        if(rarity == -1) {
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

        if(itemType == null) {
            itemType = ItemTypes.values()[Random().nextInt(ItemTypes.values().size)];
        }

        if (itemType == ItemTypes.Special){
            if(rarity == 1){
                properties.add(generateARandomProperty())
            }
        } else { generateNonSpecialItem() }
    }

    private fun generateARandomProperty(): PropertyValue {
        val property: Properties =
            Properties.values()[Random().nextInt(Properties.values().size)]
        val rarityValue: RarityValue =
            property.detail.rarities.find { value -> value.rarity == 2 } ?: return generateARandomProperty()
        return PropertyValue(property, (rarityValue.minValue..rarityValue.maxValue).random())
    }

    private fun generateNonSpecialItem() {
        var numberProperties: Int = if (rarity > 1) {
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
            if (properties.find {rarityValueOwned -> rarityValueOwned.property == property} != null){
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
        var percentage = (0..100).random()
        return when (percentage) {
            in 0..percentageRarOne -> 1
            in percentageRarOne + 1..percentageRarTwo -> 2
            else -> 3
        }
    }

    fun createBitmap() {

    }

    fun paintFightView() {

    }

    fun updateShopView() {

    }

    fun clone(): Item {
        val item = Item(context, round, rarity, itemType, cost,
            properties.clone() as ArrayList<PropertyValue>)
        item.createBitmap()
        return item
    }
}