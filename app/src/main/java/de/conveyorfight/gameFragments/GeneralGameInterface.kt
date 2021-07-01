package de.conveyorfight.gameFragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.fight.FightFragment
import de.conveyorfight.shop.ShopView

abstract class GeneralGameInterface: Runnable, Fragment(){

    var round: Int = 0

    private val gameThread = Thread(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameThread.start()
    }

    override fun run() {
        while (getPlayerHP() > 0 || getEnemyHP() > 0){

            ShopView(requireContext(), getShopItems(), getPlayerCoin(), handlePlayerBuy, handlePlayerItemReservation, handlePlayerUnreserveItem)

            //After 30 sec the FightView is called
            val handler = Handler()
            handler.postDelayed(Runnable {
                FightFragment(isPlayerFirst(), getPlayerItems(), getPlayerAfterDamage(), getEnemyItems(),getEnemyAfterDamage())
            }, 30000)
            round += 1
        }
        if(getPlayerHP() > 0){
            handleWin()
        } else {
            handleLoose()
        }
    }

    abstract fun getPlayerAfterDamage(): Character

    abstract fun getEnemyAfterDamage(): Character

    abstract fun damageToEnemy(): Any

    abstract fun damageToPlayer(): Any

    abstract fun isPlayerFirst(): Boolean

    abstract fun getPlayerCoin(): Int

    abstract fun getPlayerHP(): Int

    abstract fun getEnemyHP(): Int

    abstract fun handleWin()

    abstract fun handleLoose()

    abstract fun getPlayerItems(): Character

    abstract fun getEnemyItems(): Character

    abstract fun handlePlayerBuy(item: Item)

    abstract fun handlePlayerItemReservation(item: Item)

    abstract fun handlePlayerUnreserveItem(item: Item)

    abstract fun getShopItems(): List<Item>
}