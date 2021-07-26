package de.conveyorfight.shop

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView
import de.conveyorfight.R
import de.conveyorfight.assets.Conveyor
import de.conveyorfight.assets.GrapplingHook
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import kotlin.math.ceil
import kotlin.reflect.KFunction1

class ShopView(
    context: Context,
    val shopItems: List<Item>,
    private val playerCoin: Int,
    private val player: Character,
    handlePlayerBuy: KFunction1<Item, Unit>,
    handlePlayerItemReservation: KFunction1<Item, Unit>,
    handlePlayerUnreserveItem: KFunction1<Item, Unit>)
    : SurfaceView(context), Runnable{

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()
    private val size = Point()
    private val conveyor: Conveyor
    private var hook: GrapplingHook
    private val firstItemCurrentPosition: RectF
    private val firstItemEndPosition: RectF
    private var itemInSpeed = 100f

    private var background: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background_wall)

    init {
        display.getSize(size)
        this.createBackgroundBitmap()
        conveyor = Conveyor(context, size)
        hook = GrapplingHook(context, size)

        val screenWidth: Float = size.x.toFloat()
        val screenHeight: Float = size.y.toFloat()

        firstItemEndPosition = RectF(
            screenWidth/20,
            (screenHeight - conveyor.position.top) + screenWidth/10,
            (screenWidth/20) + screenWidth/10,
            screenHeight - conveyor.position.top
        )
        firstItemCurrentPosition = RectF()
    }

    override fun run() {
        var fps: Long = 0


        while (true) {
            val startFrameTime = System.currentTimeMillis()
            update(fps)
            draw()

            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
        TODO("Zeichne alle Items")
        TODO("Mache kaufen möglich")
        TODO("zugreifen auf wie viel Zeit übrig bleibt")
    }

    private fun update(fps: Long) {
        if(firstItemCurrentPosition.left < firstItemEndPosition.left){
            firstItemCurrentPosition.left = minOf(firstItemEndPosition.left, (firstItemCurrentPosition.left+itemInSpeed/fps))
            firstItemCurrentPosition.right = minOf(firstItemEndPosition.right, (firstItemCurrentPosition.right+itemInSpeed/fps))
        }

    }

    private fun draw() {

        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()

            canvas.drawBitmap(background, 0F, 0F, null)
            canvas.drawBitmap(conveyor.conveyor, null, conveyor.position, null)
            canvas.drawBitmap(hook.hook, null, hook.position, null)

            val screenHeight: Float = size.y.toFloat()
            val textsize = screenHeight/20
            paint.textSize = textsize

            var i = 0
            while (i < shopItems.size){
                val screenWidth: Float = size.x.toFloat()
                val currentItemPosition = RectF(
                    firstItemCurrentPosition.left + (screenWidth/5) * i,
                    firstItemCurrentPosition.top,
                    firstItemCurrentPosition.right + (screenWidth/5) * i,
                    firstItemCurrentPosition.bottom
                )

                val currentItem = shopItems[i]
                canvas.drawBitmap(currentItem.bitmap, null, currentItemPosition, null)

                paint.color = Color.argb(255, 255, 255, 255)
                var j = 0
                while ( j < currentItem.properties.size){
                    val currentProperty = currentItem.properties[j]
                    val text = if(player.propertiesKnown.contains(currentProperty.property)) {
                        "${currentProperty.property}: ${currentProperty.value}"
                    } else {
                        "???"
                    }
                    canvas.drawText(text,
                        currentItemPosition.left,
                        currentItemPosition.top + (textsize + 3) * (j + 1),
                        paint)
                    j++
                }
                paint.color = Color.argb(255, 255, 255, 255)
                canvas.drawText("cost: ${currentItem.cost}",
                    currentItemPosition.left,
                    currentItemPosition.top + (textsize + 3) * (j + 1),
                    paint)
                i++
            }


            //TODO Pfeile und Informationen malen

            holder.unlockCanvasAndPost(canvas)
        }
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

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        return true
    }

}