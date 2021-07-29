package de.conveyorfight.shop

import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceView
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Conveyor
import de.conveyorfight.assets.GrapplingHook
import de.conveyorfight.assets.Item
import java.util.ArrayList
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1


//TODO: change button colors, background of music stuff
class ShopView(
    context: Context,
    val shopItems: List<Item>,
    private val playerCoin: Int,
    private val player: Character,
    val handlePlayerBuy: KFunction1<Item, Unit>,
    val handlePlayerItemReservation: KFunction1<Item, Unit>,
    val handlePlayerUnreserveItem: KFunction0<Unit>
    )
    : SurfaceView(context), Runnable{

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    private var size = DisplayMetrics()
    private var hook: GrapplingHook
    private val conveyor: Conveyor
    private var background: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background_wall)


    private val firstItemCurrentPosition: RectF
    private val firstItemEndPosition: RectF
    private var itemInSpeed = 100f
    private val itemsBoughtIndexList = ArrayList<Int>()

    private val gameThread = Thread(this)

    init {
        //TODO: get Display metrics
        display?.apply {
            getRealMetrics(size)
            println("s")
        }
        size.heightPixels = 1080
        size.widthPixels = 2220
        println(size)
        this.createBackgroundBitmap()
        conveyor = Conveyor(context, size)
        hook = GrapplingHook(context, size)

        val screenWidth: Float = size.widthPixels.toFloat()

        firstItemEndPosition = RectF(
            screenWidth/20,
            (conveyor.position.top) - screenWidth/10,
            (screenWidth/20) + screenWidth/10,
            conveyor.position.top
        )
        firstItemCurrentPosition = RectF(
            firstItemEndPosition.left - (screenWidth),
            firstItemEndPosition.top,
            firstItemEndPosition.right - (screenWidth),
            firstItemEndPosition.bottom
        )
        gameThread.start()
    }

    override fun run() {
        var fps: Long = 0
        var lastConveyorChange = System.currentTimeMillis()
        while (true) {
            val startFrameTime = System.currentTimeMillis()

            if(firstItemCurrentPosition.left < firstItemEndPosition.left){
                if ((startFrameTime - lastConveyorChange) > 1000){
                    conveyor.isFirstPicture = !conveyor.isFirstPicture
                    lastConveyorChange = startFrameTime
                }
            }

            if (holder.surface.isValid) {
                update(fps)
                draw()
            }

            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
    }

    private fun update(fps: Long) {
        if(firstItemCurrentPosition.left < firstItemEndPosition.left){
            firstItemCurrentPosition.left = minOf(firstItemEndPosition.left, (firstItemCurrentPosition.left+itemInSpeed/fps))
            firstItemCurrentPosition.right = minOf(firstItemEndPosition.right, (firstItemCurrentPosition.right+itemInSpeed/fps))
        }

        hook.update(fps)
    }

    private fun draw() {
         canvas = holder.lockCanvas()

        canvas.drawBitmap(background, 0F, 0F, null)
        canvas.drawBitmap(hook.hook, null, hook.currentPosition, null)
        canvas.drawBitmap(conveyor.getConveyor(), null, conveyor.position, null)

        val screenHeight: Float = size.heightPixels.toFloat()
        val textsize = screenHeight/20
        paint.textSize = textsize

        drawItems(textsize)
        drawCoins(textsize)

        //TODO Interactions malen
        holder.unlockCanvasAndPost(canvas)
    }

    private fun drawCoins(textsize: Float) {
        val screenHeight = size.heightPixels.toFloat()
        val screenWidth = size.widthPixels.toFloat()
        val screenHalf = screenWidth/2

        val ovalPosition = RectF (
            screenHalf - (screenWidth/20),
            (screenHeight - (textsize * 1.5)).toFloat(),
            screenHalf + (screenWidth/20),
            (screenHeight - (textsize * 0.5)).toFloat())

        paint.color = Color.argb(255, 240, 240, 40)
        canvas.drawOval( ovalPosition, paint)

        val textPaint = Paint()
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(
            "$playerCoin",
            screenHalf,
            ((screenHeight - (textsize * 1.5)).toFloat()),
            paint
        )
    }

    private fun drawItems(textsize: Float) {
        val screenWidth: Float = size.widthPixels.toFloat()
        var i = 0
        while (i < shopItems.size) {
            if(!itemsBoughtIndexList.contains(i)) {
                val currentItemPosition = RectF(
                    firstItemCurrentPosition.left + (screenWidth / 5) * i,
                    firstItemCurrentPosition.top,
                    firstItemCurrentPosition.right + (screenWidth / 5) * i,
                    firstItemCurrentPosition.bottom
                )

                val currentItem = shopItems[i]
                val itemBitmap = Bitmap.createScaledBitmap(
                    currentItem.bitmap,
                    (screenWidth / 10).toInt(),
                    (screenWidth / 10).toInt(),
                    false
                )
                canvas.drawBitmap(itemBitmap, null, currentItemPosition, null)

                paint.color = Color.argb(255, 255, 255, 255)
                var j = 0
                while (j < currentItem.properties.size) {
                    val currentProperty = currentItem.properties[j]
                    val text = if (player.propertiesKnown.contains(currentProperty.property)) {
                        "${currentProperty.property}: ${currentProperty.value}"
                    } else {
                        "???"
                    }
                    canvas.drawText(
                        text,
                        currentItemPosition.left,
                        currentItemPosition.top - (textsize + 3) * (j + 1),
                        paint
                    )
                    j++
                }
                paint.color = Color.argb(255, 240, 240, 40)
                canvas.drawText(
                    "cost: ${currentItem.cost}",
                    currentItemPosition.left,
                    currentItemPosition.top - (textsize + 3) * (j + 1),
                    paint
                )
            }
            i++
        }
    }

    private fun createBackgroundBitmap () {
        val screenWidth: Double = size.widthPixels.toDouble()
        val screenHeight: Double = size.heightPixels.toDouble()
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
        println(backgroundHeight)
        println(backgroundWidth)

        background = Bitmap.createScaledBitmap(background, backgroundWidth, backgroundHeight, true)

    }

    //TODO when hook moving disable buttons
    private fun handleBuy() {
        hook.endPosition = RectF(
            hook.currentPosition.left,
            firstItemEndPosition.bottom - hook.hook.height,
            hook.currentPosition.right,
            firstItemEndPosition.bottom
        )
        val screenWidth = size.widthPixels
        val itemIndex = ((firstItemCurrentPosition.left - screenWidth/20) / (screenWidth/10))
        hook.shouldBounce = true
        itemsBoughtIndexList.add(itemIndex.roundToInt())
        handlePlayerBuy(shopItems[itemIndex.roundToInt()])
    }

    private fun handleReserve() {
        hook.endPosition = RectF(
           hook.currentPosition.left,
           firstItemEndPosition.bottom - hook.hook.height,
           hook.currentPosition.right,
           firstItemEndPosition.bottom
        )
        val screenWidth = size.widthPixels
        val itemIndex = ((firstItemCurrentPosition.left - screenWidth/20) / (screenWidth/10))
        handlePlayerItemReservation(shopItems[itemIndex.roundToInt()])
    }

    private fun handleUnreserve() {
        val screenHeight = size.heightPixels
        hook.endPosition = RectF(
            hook.currentPosition.left,
            ((screenHeight/ 4) - hook.hook.height).toFloat(),
            hook.currentPosition.right,
            (screenHeight/ 4).toFloat()
        )
        handlePlayerUnreserveItem()
    }

    private fun handleMoving(isMovingToTheRight: Boolean) {
        val screenWidth = size.widthPixels
        val deltaX = screenWidth/10
        hook.endPosition = RectF(
            if(isMovingToTheRight) hook.currentPosition.left + deltaX else hook.currentPosition.left - deltaX,
            hook.currentPosition.top,
            if(isMovingToTheRight) hook.currentPosition.right + deltaX else hook.currentPosition.right - deltaX,
            hook.currentPosition.bottom
        )
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        return true
    }

}