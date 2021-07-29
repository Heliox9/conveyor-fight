package de.conveyorfight.gameFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.assets.ItemTypes
import de.conveyorfight.gameFragments.online.ClientThread
import de.conveyorfight.gameFragments.online.GameState
import de.conveyorfight.gameFragments.online.ItemSelection

class OnlineGameFragment : GeneralGameInterface() {
    lateinit var state: GameState
    lateinit var items: ItemSelection
    lateinit var gsonBuilder: GsonBuilder
    lateinit var gson: Gson
    lateinit var thread: ClientThread


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // setup gson to convert from/to json
        gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(ItemTypes::class.java, ItemTypes.Deserializer())
        gson = gsonBuilder.create()

        thread = ClientThread()
        thread.start()

        Thread.sleep(1000)


        // after this point round iteration begins

        return super.onCreateView(inflater, container, savedInstanceState)
    }


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

    override fun handlePlayerUnreserveItem() {
        TODO("Not yet implemented")
    }


    override fun getShopItems(): List<Item> {
        TODO("Not yet implemented")
    }

    override fun handleRoundEnd() {
        TODO("Not yet implemented")
    }
}