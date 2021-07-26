package de.conveyorfight.fight

import android.content.Context
import android.graphics.*
import android.view.SurfaceView
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Conveyor
import de.conveyorfight.assets.GrapplingHook
import kotlin.math.ceil

// TODO: aus clonen companion objekte erstellen


class FightView(
    context: Context,
    playerFirst: Boolean,
    playerItems: Character,
    playerAfterDamage: Character,
    enemyItems: Character,
    enemyAfterDamage: Character,
    kFunction0: () -> Unit,
    kFunction01: () -> Unit
) : SurfaceView(context) {

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()
    private val size = Point()
    private val conveyor: Conveyor
    private var hook: GrapplingHook

    private var background: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background_fight)

    init {
        display.getSize(size)
        this.createBackgroundBitmap()
        conveyor = Conveyor(context, size)
        hook = GrapplingHook(context, size)
    }

    private fun createBackgroundBitmap () {
        val screenWidth: Double = size.x.toDouble()
        val screenHeight: Double = size.y.toDouble()
        val bitmapWidth: Double = background.width.toDouble()
        val bitmapHeight: Double = background.height.toDouble()
        val backgroundWidth: Int
        val backgroundHeight: Int

        val heightRatio =  screenHeight/bitmapHeight
        val widthRatio = screenWidth/bitmapWidth
        if( widthRatio > heightRatio ){
            backgroundWidth = screenWidth.toInt()
            backgroundHeight = ceil(bitmapHeight * widthRatio).toInt()
        } else {
            backgroundWidth = ceil(bitmapWidth * heightRatio).toInt()
            backgroundHeight = screenHeight.toInt()
        }

        background = Bitmap.createScaledBitmap(background, backgroundWidth, backgroundHeight, true)

    }
}