package de.conveyorfight.gameFragments

import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import java.util.*


class AiGameFragment() : GeneralGameInterface() {

    var playerCoins = 5
    var playerReservedItem: Item? = null
    var playerCharacter = Character()
    var playerPropertiesKnown = ArrayList<Item>()

    var enemyCoins = 5
    var enemyCharacter = Character()

    override fun getShopItems(): List<Item> {
        var shopItems = ArrayList<Item>()
        for (i in 5 downTo 0 step 1){
            shopItems.add(Item(requireContext(), this.round))
        }
        return shopItems
    }

    override fun getPlayerAfterDamage(): Character {
        TODO("Not yet implemented")
    }

    override fun getEnemyAfterDamage(): Character {
        TODO("Not yet implemented")
    }

    override fun isPlayerFirst(): Boolean {
        return playerCharacter.isFirst(enemyCharacter.weapon)
    }

    override fun getPlayerCoin(): Int {
        return playerCoins
    }

    override fun getPlayerItems(): Character {
        return playerCharacter
    }

    override fun getEnemyItems(): Character {
        //TODO: generate EnemyItems
        return enemyCharacter
    }

    override fun handlePlayerBuy(item: Item) {
        playerCharacter.add(item)
        playerCoins -= item.cost
    }

    override fun handlePlayerItemReservation(item: Item) {
        playerReservedItem = item
    }

    override fun handlePlayerUnreserveItem(item: Item) {
        playerReservedItem = null
    }

    override fun getPlayerHP(): Int {
        return playerCharacter.hp
    }

    override fun getEnemyHP(): Int {
        return enemyCharacter.hp
    }

    override fun handleWin() {
        TODO("Not yet implemented")
    }

    override fun handleLoose() {
        TODO("Not yet implemented")
    }
}
