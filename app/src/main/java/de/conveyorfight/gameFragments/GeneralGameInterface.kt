package de.conveyorfight.gameFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.fight.FightView
import de.conveyorfight.shop.ShopView

abstract class GeneralGameInterface: Fragment(){

    var round: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        return getShopView()
    }

    fun getShopView (): ShopView {
        return ShopView(requireContext(),
            getShopItems(),
            getPlayerCoin(),
            getPlayerItems(),
            ::handlePlayerBuy,
            ::handlePlayerItemReservation,
            ::handlePlayerUnreserveItem)
        //TODO: round iteration
    }

    fun runFightView () {
        FightView(requireContext(),
            isPlayerFirst(),
            getPlayerItems(),
            getPlayerAfterDamage(),
            getEnemyItems(),
            getEnemyAfterDamage(),
            ::handleWin,
            ::handleLoose
        )
        round++
        //TODO: loose/win sollten danach wieder auf menu verweisen
    }

    abstract fun getPlayerAfterDamage(): Character

    abstract fun getEnemyAfterDamage(): Character

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

    abstract fun handlePlayerUnreserveItem()

    abstract fun getShopItems(): List<Item>

    abstract fun handleRoundEnd()
}