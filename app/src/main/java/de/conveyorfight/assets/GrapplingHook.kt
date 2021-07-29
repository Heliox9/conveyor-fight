package de.conveyorfight.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.util.DisplayMetrics
import de.conveyorfight.R
import kotlin.math.roundToInt

class GrapplingHook(context: Context, private val size: DisplayMetrics) {
    var hook: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.grab_hook)

    var hookMoving: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.grabhook_moving)

    var currentHook: Bitmap

    var currentPosition: RectF

    var endPosition: RectF

    var shouldBounce = false

    private val speed = 100F

    init {
        val screenWidth: Double = size.widthPixels.toDouble()
        val screenHeight: Double = size.heightPixels.toDouble()
        val bitmapWidth: Double = hook.width.toDouble()
        val bitmapHeight: Double = hook.height.toDouble()

        val hookWidth = screenWidth/7
        val ratio = hookWidth/ bitmapWidth
        val hookHeight =  bitmapHeight * ratio

        hook = Bitmap.createScaledBitmap(hook,
            hookWidth.roundToInt(),
            hookHeight.roundToInt(),
            false)

        hookMoving = Bitmap.createScaledBitmap(hookMoving,
            hookWidth.roundToInt(),
            hookHeight.roundToInt(),
            false)

        currentHook = hook

        currentPosition = RectF(
            (screenWidth/20).toFloat(),
            ((screenHeight/ 4) - hookHeight).toFloat(),
            ((screenWidth/20)+hookWidth).toFloat(),
            (screenHeight/ 4).toFloat())

        endPosition = currentPosition
    }

    fun update(fps: Long){
        when {
            currentPosition.left - endPosition.left > 0 -> {
                currentPosition.left = maxOf(endPosition.left, (currentPosition.left - speed/fps))
                currentPosition.right = maxOf(endPosition.right, (currentPosition.right - speed/fps))
                currentHook = if(currentPosition == endPosition) hook else createFlippedBitmap(hookMoving)
            }
            currentPosition.left - endPosition.left < 0 -> {
                currentPosition.left = minOf(endPosition.left, (currentPosition.left + speed/fps))
                currentPosition.right = minOf(endPosition.right, (currentPosition.right + speed/fps))
                currentHook = if(currentPosition == endPosition) hook else hookMoving
            }
            currentPosition.top - endPosition.top < 0 -> {
                currentPosition.top = minOf(endPosition.top, (currentPosition.top + speed/fps))
                currentPosition.bottom = minOf(endPosition.bottom, (currentPosition.bottom + speed/fps))
                if(currentHook != hook) currentHook = hook
            }
            currentPosition.top - endPosition.top > 0 -> {
                currentPosition.top = maxOf(endPosition.top, (currentPosition.top - speed/fps))
                currentPosition.bottom = maxOf(endPosition.bottom, (currentPosition.bottom - speed/fps))
                if(currentHook != hook) currentHook = hook
                if(currentPosition == endPosition && shouldBounce) run {
                    endPosition = RectF(
                        currentPosition.left,
                        ((size.heightPixels / 4) - hook.height).toFloat(),
                        currentPosition.right,
                        (size.heightPixels / 4).toFloat()
                    )
                    shouldBounce = false
                }
            }
        }
    }

    private fun createFlippedBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f
        )
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}