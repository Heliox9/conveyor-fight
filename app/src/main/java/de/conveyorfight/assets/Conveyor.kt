package de.conveyorfight.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.util.DisplayMetrics
import de.conveyorfight.R
import kotlin.math.roundToInt

class Conveyor(context: Context, size: DisplayMetrics) {
    private var conveyorStill: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.conveyor)

    private var conveyorMoved: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.conveyor2)

    val position: RectF

    var isFirstPicture = true

    init {
        val screenWidth: Double = size.widthPixels.toDouble()
        val screenHeight: Double = size.heightPixels.toDouble()
        val bitmapWidth: Double = conveyorStill.width.toDouble()
        val bitmapHeight: Double = conveyorStill.height.toDouble()

        val conveyorHeight: Double
        val conveyorWidth: Double

        val heightRatio =  screenHeight/ (4*bitmapHeight)
        val widthRatio = screenWidth / bitmapWidth
        if( widthRatio > heightRatio ){
            conveyorWidth = screenWidth
            conveyorHeight = bitmapHeight * widthRatio
        } else {
            conveyorWidth = bitmapWidth * heightRatio
            conveyorHeight = screenHeight / 4
        }

        conveyorStill = Bitmap.createScaledBitmap(conveyorStill,
            conveyorWidth.roundToInt(),
            conveyorHeight.roundToInt(),
            false)

        conveyorMoved = Bitmap.createScaledBitmap(conveyorMoved,
            conveyorWidth.roundToInt(),
            conveyorHeight.roundToInt(),
            false)

        position = RectF(
            ((screenWidth - screenHeight/10) - conveyorWidth).toFloat(),
            (screenHeight - conveyorHeight).toFloat(),
            (screenWidth - screenHeight/10).toFloat(),
            screenHeight.toFloat())
    }

    fun getConveyor(): Bitmap {
        return if(isFirstPicture) conveyorStill else conveyorMoved
    }
}