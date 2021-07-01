package de.conveyorfight.assets

import kotlin.random.Random

class Character {

    var hp = 100

    var helmet: Item? =null
    var gloves: Item? =null
    var armor: Item? =null
    var pants: Item? =null
    var shoes: Item? =null
    var special: Item? =null
    var weapon: Item? =null

    fun add(item: Item){
        when(item.itemType){
            ItemTypes.Helmet -> helmet = item
            ItemTypes.Gloves -> gloves = item
            ItemTypes.Armor -> armor = item
            ItemTypes.Pants -> pants = item
            ItemTypes.Shoes -> shoes = item
            ItemTypes.Potion -> special = item
            ItemTypes.Wand -> special = item
            ItemTypes.Shield -> special = item
            else -> weapon = item
        }
    }

    fun isFirst(enemyWeapon: Item?): Boolean{

        val playerWeapon = weapon
        if(enemyWeapon == null && playerWeapon == null){
            return isFirstByLuck()
        }
        if(enemyWeapon == null){
            return true
        }
        if(playerWeapon == null){
            return false
        }

        val enemyRange = enemyWeapon.itemType.range
        val playerRange = playerWeapon.itemType.range

        if(enemyRange == playerRange){
            if(enemyWeapon.rarity == playerWeapon.rarity){
                return isFirstByLuck()
            }
            return playerWeapon.rarity > enemyWeapon.rarity
        }
        return playerRange > enemyRange
    }

    private fun isFirstByLuck(): Boolean{
        return Random.nextBoolean()
    }

    fun getDamageDealt(): {
        var itemList = listOf<Item?>(helmet, gloves, armor, pants, shoes, special, weapon)
        for (item in itemList){
            if(item != null){

            }
        }
    }

    fun calculateDamageDone() {

    }
}