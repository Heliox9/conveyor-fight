package de.conveyorfight.gameFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.fight.FightView
import de.conveyorfight.shop.ShopView

abstract class GeneralGameInterface : Fragment() {

    var round: Int = 1
    var switcher: ViewSwitcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        //TODO: unter Umständen mache eine Rebootfunktion für die ShopView
        /*switcher = ViewSwitcher(requireContext())
        switcher!!.addView(getFightView())
        switcher!!.addView(getShopView())
        return switcher!!.currentView */
        return getShopView()
    }

    private fun switchView() {
        switcher!!.showNext()
    }

    private fun getShopView(): ShopView {
        return ShopView(
            requireContext(),
            getShopItems(),
            getPlayerCoin(),
            getPlayerItems(),
            ::handlePlayerBuy,
            ::handlePlayerItemReservation,
            ::handlePlayerUnreserveItem,
            ::switchView
        )
    }

    private fun getFightView(): FightView {
        round++
        return FightView(
            requireContext(),
            isPlayerFirst(),
            getPlayerItems(),
            getPlayerAfterDamage(),
            getEnemyItems(),
            getEnemyAfterDamage(),
            ::handleWin,
            ::handleLoose,
            ::handleGameEnd,
            ::switchView
        )
    }

    private fun handleGameEnd() {
        customHandleGameEnd()
        Navigation.findNavController(requireView()).navigate(R.id.menuFragment)
    }

    abstract fun handleShopFinished()

    abstract fun customHandleGameEnd()

    abstract fun getPlayerAfterDamage(): Character

    abstract fun getEnemyAfterDamage(): Character

    abstract fun isPlayerFirst(): Boolean

    abstract fun getPlayerCoin(): Int


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