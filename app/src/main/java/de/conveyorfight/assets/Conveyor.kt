package de.conveyorfight.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.RectF
import de.conveyorfight.R
import kotlin.math.roundToInt

class Conveyor(context: Context, size: Point) {
    var conveyor: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.conveyor)

    val position: RectF

    init {
        val screenWidth: Double = size.x.toDouble()
        val screenHeight: Double = size.y.toDouble()
        val bitmapWidth: Double = conveyor.width.toDouble()
        val bitmapHeight: Double = conveyor.height.toDouble()

        val conveyorHeight =  screenHeight/ 4
        val ratio = conveyorHeight/ bitmapHeight
        val conveyorWidth = bitmapWidth * ratio

        conveyor = Bitmap.createScaledBitmap(conveyor,
            conveyorWidth.roundToInt(),
            conveyorHeight.roundToInt(),
            false)

        position = RectF(
            (screenWidth * 0.75 - conveyorWidth).toFloat(),
            (screenHeight - conveyorHeight).toFloat(),
            (screenWidth * 0.75).toFloat(),
            screenHeight.toFloat())
    }
}