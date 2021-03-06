package de.conveyorfight.gameFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.assets.PropertyValue
import de.conveyorfight.gameFragments.online.ClientThread
import de.conveyorfight.gameFragments.online.GameState
import de.conveyorfight.gameFragments.online.ItemSelection
import java.util.*

/**
 * GameFragment implementation for online versus mode
 */
class OnlineGameFragment : GeneralGameInterface() {
    private val name = UUID.randomUUID().toString() // random name for the player to reference
    private lateinit var gson: Gson
    private var thread: ClientThread? = null
    private var stateBeforeDamage: GameState? = null
    private var stateAfterDamage: GameState? = null
    private var first: Boolean = true

    private lateinit var character: Character
    private var money: Int = -42

    /**
     * expanded view creation with boilerplate for gson and socket thread
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // setup gson to convert from/to json
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            Character::class.java,
            Character.Deserializer(requireContext())
        )
        gsonBuilder.registerTypeAdapter(Item::class.java, Item.Deserializer(requireContext()))
        gsonBuilder.registerTypeAdapter(Item::class.java, Item.Serializer())
        gsonBuilder.registerTypeAdapter(PropertyValue::class.java, PropertyValue.Deserializer())
        gsonBuilder.registerTypeAdapter(
            de.conveyorfight.assets.Properties::class.java,
            de.conveyorfight.assets.Properties.Deserializer()
        )

        gson = gsonBuilder.create()

        thread = ClientThread(name, getString(R.string.ip), getString(R.string.port).toInt())
        thread!!.start()

        // wait for thread to initialize
        Thread.sleep(100)

        // after this point round iteration begins

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun handleShopFinished() {
        println("\n\nhandle shop finished\n\n")

        val json = gson.toJson(itemSelection)
        println("json: $json")
        thread?.addOutgoing(json)

        // read game state
        stateBeforeDamage = gson.fromJson(thread?.getNextIncoming(), GameState::class.java)
        stateAfterDamage = gson.fromJson(thread?.getNextIncoming(), GameState::class.java)
        first = gson.fromJson(thread?.getNextIncoming(), Boolean::class.java)
    }

    override fun customHandleGameEnd() {
        println("custom game end")
        thread!!.shutdown()
        thread = null

        Navigation.findNavController(requireView())
            .navigate(R.id.action_onlineGameFragment_to_menuFragment)
    }


    override fun getPlayerAfterDamage(): Character {
        println("getPlayerAfterDamage")
        return stateAfterDamage!!.player
    }

    override fun getEnemyAfterDamage(): Character {
        println("getEnemyAfterDamage")
        return stateAfterDamage!!.opponent
    }

    override fun isPlayerFirst(): Boolean {
        println("isPlayerFirst")
        return first
    }

    /**
     * read money individually from socket
     */
    override fun getPlayerCoin(): Int {
        println("getter money called")
        money = gson.fromJson(thread?.getNextIncoming(), Int::class.java)
        return money
    }


    /**
     * read character sheet from socket
     */
    override fun getPlayerItems(): Character {
        println("getPlayerItems")
        if (stateBeforeDamage != null) {
            println("returning character from game state")
            return stateBeforeDamage!!.player
        }

        println("attempting to read player character")
        val message = thread?.getNextIncoming()
        println(message)
        character = gson.fromJson(message, Character::class.java)
        println("player : $character")
        return character
    }

    override fun getEnemyItems(): Character {
        println("getEnemyItems")
        println("returning character from game state")
        return stateBeforeDamage!!.opponent
    }

    /**
     * add item to buy list and send selection over socket
     */
    override fun handlePlayerBuy(item: Item) {
        println("handlePlayerBuy")
        itemSelection.bought.add(item)
        println("bought added: $itemSelection")
    }

    /**
     * add item to saved list
     */
    override fun handlePlayerItemReservation(item: Item) {
        println("handlePlayerItemReservation")
        itemSelection.saved.add(item)
    }

    /**
     * clear saved list
     */
    override fun handlePlayerUnreserveItem() {
        println("handlePlayerUndoReserveItem")
        itemSelection.saved.clear()
    }

    private lateinit var itemSelection: ItemSelection

    /**
     * read selection from socket
     */
    override fun getShopItems(): List<Item> {
        println("getting items")
        itemSelection = gson.fromJson(thread?.getNextIncoming(), ItemSelection::class.java)
        println("decoded: $itemSelection")
        return itemSelection.selection
    }

    override fun handleRoundEnd() {
        println("handleRoundEnd")
        stateBeforeDamage = null
        stateAfterDamage = null
    }
}