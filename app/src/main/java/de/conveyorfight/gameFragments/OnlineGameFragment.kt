package de.conveyorfight.gameFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.assets.PropertyValue
import de.conveyorfight.gameFragments.online.ClientThread
import de.conveyorfight.gameFragments.online.ItemSelection

class OnlineGameFragment : GeneralGameInterface() {
    private lateinit var gsonBuilder: GsonBuilder
    private lateinit var gson: Gson
    private lateinit var thread: ClientThread

    private lateinit var character: Character
    private var money: Int = -42

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // setup gson to convert from/to json
        gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Item::class.java, Item.Deserializer(requireContext()))
        gsonBuilder.registerTypeAdapter(PropertyValue::class.java, PropertyValue.Deserializer())
        gson = gsonBuilder.create()



        thread = ClientThread()
        thread.start()

        // wait for thread to initialize
        Thread.sleep(100)


        // after this point round iteration begins

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun getPlayerAfterDamage(): Character {
        println("getPlayerAfterDamage")
        TODO("Not yet implemented")
    }

    override fun getEnemyAfterDamage(): Character {
        println("getEnemyAfterDamage")
        TODO("Not yet implemented")
    }

    override fun isPlayerFirst(): Boolean {
        println("isPlayerFirst")
        TODO("Not yet implemented")
    }

    override fun getPlayerCoin(): Int {
        println("getter money called")
        money = gson.fromJson(thread.getNextIncoming(), Int::class.java)
        return money
    }

    override fun getPlayerHP(): Int {
        println("getPlayerHP")
        TODO("Not yet implemented")
    }

    override fun getEnemyHP(): Int {
        println("getEnemyHP")
        TODO("Not yet implemented")
    }

    override fun handleWin() {
        println("handleWin")
        TODO("Not yet implemented")
    }

    override fun handleLoose() {
        println("handleLoose")
        TODO("Not yet implemented")
    }

    override fun getPlayerItems(): Character {
        println("getPlayerItems")
        character = gson.fromJson(thread.getNextIncoming(), Character::class.java)
        println("player: $character")
        return character
    }

    override fun getEnemyItems(): Character {
        println("getEnemyItems")

        TODO("Not yet implemented")
    }

    override fun handlePlayerBuy(item: Item) {
        println("handlePlayerBuy")
        itemSelection.bought.add(item)
        thread.addOutgoing(gson.toJson(itemSelection))
    }

    override fun handlePlayerItemReservation(item: Item) {
        println("handlePlayerItemReservation")
        itemSelection.saved.add(item)
    }

    override fun handlePlayerUnreserveItem() {
        println("handlePlayerUnreserveItem")
        itemSelection.saved.clear()
    }

    private lateinit var itemSelection: ItemSelection
    override fun getShopItems(): List<Item> {
        println("getting items")
        itemSelection = gson.fromJson(thread.getNextIncoming(), ItemSelection::class.java)
        println("decoded: $itemSelection")
        return itemSelection.selection
    }

    override fun handleRoundEnd() {
        println("handleRoundEnd")
        TODO("Not yet implemented")
    }
}