package de.conveyorfight.gameFragments

import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.assets.ItemTypes
import java.util.*

class AiGameFragment() : GeneralGameInterface() {

    var playerCoins = 5
    var playerReservedItem: Item? = null
    var playerCharacter = Character()

    var enemyCharacter = Character()


    override fun getShopItems(): List<Item> {
        val shopItems = ArrayList<Item>()
        val numberOfItemsToGenerate = if (playerReservedItem == null) 4 else 3
        for (i in numberOfItemsToGenerate downTo 0 step 1){
            shopItems.add(Item(requireContext(), this.round))
        }
        if (playerReservedItem != null){
            shopItems.add(playerReservedItem!!)
            playerReservedItem = null
        }
        return shopItems
    }

    override fun getPlayerAfterDamage(): Character {
        return calculateDamage(enemyCharacter, playerCharacter)
    }

    override fun getEnemyAfterDamage(): Character {
        return calculateDamage(playerCharacter, enemyCharacter)
    }

    private fun calculateDamage(attacker: Character, attacked: Character): Character {
        if (attacked.special != null && attacked.special!!.rarity == 3) {
            return attacked
        }
        if (attacker.special != null && attacker.special!!.rarity == 3) {
            attacked.calculateDamageTaken(attacked.getDamageDealt())
            return attacked
        }
        attacked.calculateDamageTaken(attacker.getDamageDealt())
        return attacked
    }

    override fun isPlayerFirst(): Boolean {
        return playerCharacter.isFirst(enemyCharacter.weapon)
    }

    override fun getPlayerCoin(): Int {
        return playerCoins
    }

    override fun getPlayerItems(): Character {
        return playerCharacter.clone()
    }

    override fun getEnemyItems(): Character {
        val item = Item(requireContext(), round)
        if(item.itemType == ItemTypes.Special){
            return getEnemyItems()
        }
        enemyCharacter.add(item)
        return enemyCharacter.clone()
    }

    override fun handlePlayerBuy(item: Item) {
        if (playerCoins < item.cost) return
        if (item.itemType == ItemTypes.Special && item.rarity == 2) {
            if(!playerCharacter.isUpgradeAble()) return
            playerCharacter.upgradeRandomItem()
        } else {
            playerCharacter.add(item)
        }
        playerCoins -= item.cost
    }

    override fun handlePlayerItemReservation(item: Item) {
        playerReservedItem = item
    }

    override fun handlePlayerUnreserveItem() {
        playerReservedItem = null
    }

    override fun handleRoundEnd() {
        playerCoins += 5
    }

    override fun customHandleGameEnd() {
        //for the ai there is nothing to do here
    }
}
