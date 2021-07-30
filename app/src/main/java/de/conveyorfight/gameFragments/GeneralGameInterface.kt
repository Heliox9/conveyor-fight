package de.conveyorfight.gameFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import de.conveyorfight.fight.FightView
import de.conveyorfight.shop.ShopView
import java.util.*
import kotlin.concurrent.timerTask


abstract class GeneralGameInterface : Fragment() {

    var round: Int = 1
    var switcher: ViewAnimator? = null
    var timer: Timer = Timer()
    var isShopView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        switcher = ViewAnimator(requireContext())
        switcher!!.addView(getShopView())
        createTimer(30000)
        return switcher!!
    }

    private fun switchView() {
        if (isShopView) {
            val fightView = getFightView()
            switcher!!.removeAllViews()
            switcher!!.addView(fightView)
            switcher!!.showNext()
            fightView.start()
            isShopView = false
            createTimer(10000)
        } else {
            switcher!!.addView(getShopView())
            switcher!!.showNext()
            isShopView = true
            createTimer(30000)
        }
    }

    fun createTimer(delay: Long) {
        timer.schedule(timerTask {
            activity?.runOnUiThread {
                switchView()
            }
        }, delay)
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
            ::handleShopFinished,
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
            ::handleGameEnd,
            ::handleRoundEnd
        )
    }

    private fun handleGameEnd () {
        timer.cancel()
        customHandleGameEnd()
    }

    abstract fun handleShopFinished()

    abstract fun customHandleGameEnd()

    abstract fun getPlayerAfterDamage(): Character

    abstract fun getEnemyAfterDamage(): Character

    abstract fun isPlayerFirst(): Boolean

    abstract fun getPlayerCoin(): Int

    abstract fun getPlayerItems(): Character

    abstract fun getEnemyItems(): Character

    abstract fun handlePlayerBuy(item: Item)

    abstract fun handlePlayerItemReservation(item: Item)

    abstract fun handlePlayerUnreserveItem()

    abstract fun getShopItems(): List<Item>

    abstract fun handleRoundEnd()
}