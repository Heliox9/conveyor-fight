package de.conveyorfight.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.RectF
import de.conveyorfight.R
import kotlin.math.roundToInt

class GrapplingHook(context: Context, size: Point) {
    var hook: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.grab_hook)

    val position: RectF

    init {
        val screenWidth: Double = size.x.toDouble()
        val screenHeight: Double = size.y.toDouble()
        val bitmapWidth: Double = hook.width.toDouble()
        val bitmapHeight: Double = hook.height.toDouble()

        val hookWidth = screenWidth/10
        val ratio = hookWidth/ bitmapWidth
        val hookHeight =  bitmapHeight * ratio

        hook = Bitmap.createScaledBitmap(hook,
            hookWidth.roundToInt(),
            hookHeight.roundToInt(),
            false)

        position = RectF(
            (screenWidth/20).toFloat(),
            (screenHeight/ 4).toFloat(),
            ((screenWidth/20)+hookWidth).toFloat(),
            ((screenHeight/ 4) - hookHeight).toFloat())
    }
}