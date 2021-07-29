package de.conveyorfight.gameFragments

import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import java.net.Socket

class OnlineGameFragment : GeneralGameInterface() {
    lateinit var socket: Socket

    override fun getPlayerAfterDamage(): Character {
        TODO("Not yet implemented")
    }

    override fun getEnemyAfterDamage(): Character {
        TODO("Not yet implemented")
    }

    override fun isPlayerFirst(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlayerCoin(): Int {
        TODO("Not yet implemented")
    }

    override fun getPlayerHP(): Int {
        TODO("Not yet implemented")
    }

    override fun getEnemyHP(): Int {
        TODO("Not yet implemented")
    }

    override fun handleWin() {
        TODO("Not yet implemented")
    }

    override fun handleLoose() {
        TODO("Not yet implemented")
    }

    override fun getPlayerItems(): Character {
        TODO("Not yet implemented")
    }

    override fun getEnemyItems(): Character {
        TODO("Not yet implemented")
    }

    override fun handlePlayerBuy(item: Item) {
        TODO("Not yet implemented")
    }

    override fun handlePlayerItemReservation(item: Item) {
        TODO("Not yet implemented")
    }

    override fun handlePlayerUnreserveItem(item: Item) {
        TODO("Not yet implemented")
    }

    override fun getShopItems(): List<Item> {
        TODO("Not yet implemented")
    }
}