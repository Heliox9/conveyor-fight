package de.conveyorfight.assets

import java.util.ArrayList
import kotlin.math.min
import kotlin.random.Random

class Character(var hp: Int = 100,
                var helmet: Item? =null,
                var gloves: Item? =null,
                var armor: Item? =null,
                var pants: Item? =null,
                var shoes: Item? =null,
                var special: Item? =null,
                var weapon: Item? =null,
                var propertiesKnown: ArrayList<Properties> = ArrayList<Properties>()) {

    //TODO visual (den dude muss ich ja auch zeichenen q.q)
    //TODO mergen mit Masterstand

    fun add(item: Item){
        when(item.itemType){
            ItemTypes.Helmet -> helmet = item
            ItemTypes.Gloves -> gloves = item
            ItemTypes.Armor -> armor = item
            ItemTypes.Pants -> pants = item
            ItemTypes.Shoes -> shoes = item
            ItemTypes.Special -> special = item
            else -> weapon = item
        }
        for (propertyValue in item.properties) {
            propertiesKnown.add(propertyValue.property)
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

        val enemyRange = enemyWeapon.itemType?.range
        val playerRange = playerWeapon.itemType?.range

        if(enemyRange == playerRange){
            if(enemyWeapon.rarity == playerWeapon.rarity){
                return isFirstByLuck()
            }
            return playerWeapon.rarity > enemyWeapon.rarity
        }
        return playerRange!! > enemyRange!!
    }

    private fun isFirstByLuck(): Boolean{
        return Random.nextBoolean()
    }

    fun getDamageDealt(): List<PropertyValue>{
        val itemList = listOf(helmet, gloves, armor, pants, shoes, special, weapon)
        val propertyDamage = ArrayList<PropertyValue>()
        for (item in itemList){
            if(item != null){
                for (property in item.properties){
                    if(property.property.detail.isDamage) {
                        val isAdded = propertyDamage.find { pd -> pd.property == property.property }
                        if (isAdded != null) {
                            isAdded.value += property.value
                        } else {
                            propertyDamage.add(PropertyValue(property.property, property.value))
                        }
                    }
                }
            }
        }
        return propertyDamage
    }

    fun calculateDamageTaken(propertyDamages: List<PropertyValue>) {
        val itemList = listOf(helmet, gloves, armor, pants, shoes, special, weapon)
        val propertyArmor = ArrayList<PropertyValue>()
        for (item in itemList){
            if(item != null){
                for (property in item.properties){
                    if(!property.property.detail.isDamage) {
                        val isAdded = propertyArmor.find { pd -> pd.property == property.property }
                        if (isAdded != null) {
                            isAdded.value += property.value
                        } else {
                            propertyArmor.add(PropertyValue(property.property, property.value))
                        }
                    }
                }
            }
        }
        var damage = 0
        for(propertyDamage in propertyDamages){
            val blockedBy = propertyDamage.property.detail.blockedBy
            damage += if(blockedBy != null){
                val isAdded = propertyArmor.find { pd -> pd.property == blockedBy }
                if (isAdded != null) {
                    min(0, propertyDamage.value - isAdded.value)
                } else {
                    propertyDamage.value
                }
            } else {
                propertyDamage.value
            }
        }
        hp -= damage
    }

    fun upgradeRandomItem(){
        val itemList = listOf(helmet, gloves, armor, pants, shoes, special, weapon)
        val probableItems = ArrayList<Item>()
        for (item in itemList){
            if(item != null && item.rarity < 3){
                probableItems.add(item)
            }
        }
        val itemToUpgrade = probableItems.random()
        add(Item(itemToUpgrade.context, itemToUpgrade.round, (itemToUpgrade.rarity + 1)))
    }

    fun isUpgradeAble(): Boolean {
        val itemList = listOf(helmet, gloves, armor, pants, shoes, special, weapon)
        for (item in itemList){
            if(item != null && item.rarity < 3){
                return true
            }
        }
        return false
    }

    fun clone(): Character {
        return Character(hp, helmet?.clone(), gloves?.clone(), armor?.clone(), pants?.clone(),
            shoes?.clone(), special?.clone(), weapon?.clone(),
            propertiesKnown.clone() as ArrayList<Properties>)
    }
}