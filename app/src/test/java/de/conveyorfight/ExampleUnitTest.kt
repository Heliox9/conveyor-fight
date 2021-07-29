package de.conveyorfight

import com.google.gson.GsonBuilder
import de.conveyorfight.assets.Item
import de.conveyorfight.assets.ItemTypes
import de.conveyorfight.gameFragments.online.GameState
import de.conveyorfight.gameFragments.online.ItemSelection
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test_server() {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(ItemTypes::class.java, ItemTypes.Deserializer())
        val gson = gsonBuilder.create()


        val client: Socket = Socket("127.0.0.1", 88)
        val output = PrintWriter(client.getOutputStream(), true)
        val input = BufferedReader(InputStreamReader(client.getInputStream()))
        output.println("Kotlin")
        println(input.readLine()) // game id

        println(input.readLine()) // opponent

        var state: GameState

        do {// iterate rounds
            var selection: ItemSelection =
                gson.fromJson(
                    input.readLine(),
                    ItemSelection::class.java
                )//TODO create custom json packing for itemTyp because java implementation does not match
            println("received:\n$selection")

            val bought: ArrayList<Item> = ArrayList()
            bought.add(selection.selection[4])
            val saved: ArrayList<Item> = ArrayList()
            saved.add(selection.selection[1])
            selection.bought = (bought)
            selection.saved = (saved)

            output.println(gson.toJson(selection));
            println("sent:\n$selection")

            // read fight stats
            state = gson.fromJson(input.readLine(), GameState::class.java) // read Gamestate
            println("received:")
            println(state)

        } while (state.player.hp > 0 && state.opponent.hp > 0)
        client.close()

    }
}