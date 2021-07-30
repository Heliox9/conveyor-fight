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
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1


//TODO: change button colors, background of music stuff
//TODO: FightView refactoren
//TODO: item verschwindet sofort, wenn man auf Kaufen klickt
//TODO: Loose GIF & Win Gif
class ShopView(
    context: Context,
    val shopItems: List<Item>,
    private var playerCoin: Int,
    private val player: Character,
    val handlePlayerBuy: KFunction1<Item, Unit>,
    val handlePlayerItemReservation: KFunction1<Item, Unit>,
    val handlePlayerUnreserveItem: KFunction0<Unit>
    )
    : SurfaceView(context), Runnable{

    private lateinit var rightArrowPosition: RectF
    private lateinit var leftArrowPosition: RectF
    private lateinit var buyButton: RectF
    private lateinit var reserveButton: RectF

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    private var size = DisplayMetrics()
    private lateinit var hook: GrapplingHook
    private lateinit var conveyor: Conveyor
    private var background: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background_wall)

    private var arrow: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.arrow)

    private var buttonBackground: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.button_background)


    private lateinit  var firstItemCurrentPosition: RectF
    private lateinit var firstItemEndPosition: RectF
    private var itemInSpeed = 250f
    private val itemsBoughtIndexList = ArrayList<Int>()

    private val gameThread = Thread(this)
    private var didDisplayLoad = false

    init {
        gameThread.start()
    }

    private fun init () {
        display?.apply {
            getRealMetrics(size)
        }
        size.heightPixels = 1080
        size.widthPixels = 2220

        this.createBackgroundBitmap()
        conveyor = Conveyor(context, size)
        hook = GrapplingHook(context, size)

        val screenWidth: Float = size.widthPixels.toFloat()
        val screenHeight: Float = size.heightPixels.toFloat()

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

        val arrowSize = screenWidth/20
        arrow = Bitmap.createScaledBitmap(arrow, arrowSize.roundToInt(),
            arrowSize.roundToInt(), false)

        leftArrowPosition = RectF(
            screenWidth / 20,
            screenHeight/40,
            screenWidth / 20 + arrowSize,
            screenHeight/40 + arrowSize
        )
        rightArrowPosition = RectF(
            screenWidth - leftArrowPosition.right,
            leftArrowPosition.top,
            screenWidth - leftArrowPosition.left ,
            leftArrowPosition.bottom,
        )

        val buttonLength = screenWidth/6
        val widthRatio = buttonLength/buttonBackground.width.toFloat()
        val buttonHeight = buttonBackground.height * widthRatio

        buttonBackground = Bitmap.createScaledBitmap(buttonBackground,
            buttonLength.roundToInt(), buttonHeight.roundToInt(), false)

        buyButton = RectF(
            screenWidth/8 ,
            screenHeight - (screenHeight/40 + buttonHeight),
            screenWidth/8 + buttonLength,
            screenHeight - screenHeight/40
        )
        reserveButton = RectF(
            screenWidth - buyButton.right ,
            buyButton.top,
            screenWidth - buyButton.left,
            buyButton.bottom
        )
        didDisplayLoad = true
    }

    override fun run() {
        var fps: Long = 0
        var lastConveyorChange = System.currentTimeMillis()
        val startTime = System.currentTimeMillis()
        while (true) {
            val startFrameTime = System.currentTimeMillis()

            if (holder.surface.isValid) {
                if(display != null && !didDisplayLoad){
                    init()
                }
                if(didDisplayLoad) {
                    if(firstItemCurrentPosition.left < firstItemEndPosition.left){
                        if ((startFrameTime - lastConveyorChange) > 1000){
                            conveyor.isFirstPicture = !conveyor.isFirstPicture
                            lastConveyorChange = startFrameTime
                        }
                    }

                    update(fps)
                    draw()
                }
            }

            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }

            if(System.currentTimeMillis() - startTime >= 29000){
                break
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
        canvas.drawBitmap(conveyor.getConveyor(), null, conveyor.position, null)

        val screenHeight: Float = size.heightPixels.toFloat()
        val textSize = screenHeight/25
        paint.textSize = textSize

        drawItems(textSize)
        drawCoins(textSize)
        canvas.drawBitmap(hook.currentHook,
            hook.currentPosition.left,
            hook.currentPosition.bottom - hook.currentHook.height, null)

        if(hook.currentPosition == hook.endPosition){
            drawButtons(textSize)
        }

        holder.unlockCanvasAndPost(canvas)
    }

    private fun drawButtons(textSize: Float) {
        val screenWidth = size.widthPixels.toFloat()
        val screenHeight = size.heightPixels.toFloat()

        paint.color = Color.argb(255, 240, 0, 0)

        val isHookDown = hook.endPosition.bottom > screenHeight/2
        //left Arrow
        if (hook.currentPosition.left > screenWidth / 20 && !isHookDown){
            canvas.drawBitmap(arrow, null, leftArrowPosition, null)
        }

        //rightArrow
        if (hook.currentPosition.right < screenWidth - screenWidth / 20 && !isHookDown){
            canvas.drawBitmap(createFlippedBitmap(arrow), null, rightArrowPosition, null)
        }

        val itemIndex: Int = (((hook.currentPosition.left.toDouble() - screenWidth/20) / (screenWidth/5)).roundToInt())
        val isItemBought = itemsBoughtIndexList.contains(itemIndex)
        val isItemAffordable = shopItems[itemIndex].cost <= playerCoin

        val whiteTextPaint = Paint()
        whiteTextPaint.color = Color.argb(255, 255, 255, 255)
        whiteTextPaint.textAlign = Paint.Align.CENTER
        whiteTextPaint.textSize = textSize
        //BuyButton
        if(!isHookDown && !isItemBought && isItemAffordable) {
            canvas.drawBitmap(buttonBackground, null, buyButton, null)
            val x = buyButton.left + (buyButton.right - buyButton.left)/2
            val y = buyButton.top + (buyButton.bottom - buyButton.top)/2
            canvas.drawText("Buy", x, y, whiteTextPaint)
        }

        //ReserveButton
        if (!isItemBought) {
            canvas.drawBitmap(buttonBackground, null, reserveButton, null)
            val x = reserveButton.left + (reserveButton.right - reserveButton.left)/2
            val y = reserveButton.top + (reserveButton.bottom - reserveButton.top)/2
            val text = if (isHookDown) "Unreserve" else "Reserve"
            canvas.drawText(text, x, y, whiteTextPaint)
        }
    }

    private fun drawCoins(textSize: Float) {
        val screenHeight = size.heightPixels.toFloat()
        val screenWidth = size.widthPixels.toFloat()
        val screenHalf = screenWidth/2

        val ovalPosition = RectF (
            screenHalf - (screenWidth/40),
            (screenHeight - (textSize * 3.5)).toFloat(),
            screenHalf + (screenWidth/40),
            (screenHeight - (textSize * 0.5)).toFloat())

        paint.color = Color.argb(255, 255, 140, 0)
        canvas.drawOval( ovalPosition, paint)

        val textPaint = Paint()
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = Color.argb(255, 255, 255, 255)
        textPaint.textSize = textSize
        canvas.drawText(
            "$playerCoin",
            screenHalf,
            ((screenHeight - (textSize * 1.5)).toFloat()),
            textPaint
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
                        "${currentProperty.property.name
                            .replace("_", " ")
                            .lowercase(Locale.getDefault())
                            .replaceFirstChar { it.uppercase() }}: " +
                                "${currentProperty.value}"
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

        background = Bitmap.createScaledBitmap(background, backgroundWidth, backgroundHeight, true)
    }

    private fun handleBuy() {
        val screenWidth = size.widthPixels
        hook.endPosition = RectF(
            hook.currentPosition.left,
            firstItemEndPosition.bottom - (hook.hook.height + screenWidth / 25),
            hook.currentPosition.right,
            firstItemEndPosition.bottom - screenWidth / 25
        )
        val itemIndex = ((hook.currentPosition.left.toDouble() - screenWidth/20) / (screenWidth/5))
        hook.shouldBounce = true
        itemsBoughtIndexList.add(itemIndex.roundToInt())
        handlePlayerBuy(shopItems[itemIndex.roundToInt()])
        playerCoin -= shopItems[itemIndex.roundToInt()].cost
    }

    private fun handleReserve() {
        val screenWidth = size.widthPixels
        hook.endPosition = RectF(
            hook.currentPosition.left,
            firstItemEndPosition.bottom - (hook.hook.height + screenWidth / 25),
            hook.currentPosition.right,
            firstItemEndPosition.bottom - screenWidth / 25
        )
        val itemIndex = ((hook.currentPosition.left - screenWidth/20) / (screenWidth/5))
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
        val deltaX = screenWidth/5
        hook.endPosition = RectF(
            if(isMovingToTheRight) hook.currentPosition.left + deltaX else hook.currentPosition.left - deltaX,
            hook.currentPosition.top,
            if(isMovingToTheRight) hook.currentPosition.right + deltaX else hook.currentPosition.right - deltaX,
            hook.currentPosition.bottom
        )
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action and MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            // Or moved their finger while touching screen
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN-> {
                val screenWidth = size.widthPixels
                val screenHeight = size.heightPixels
                if(hook.currentPosition == hook.endPosition) {
                    if (hook.currentPosition.left > screenWidth / 20) {
                        if(handlePossibleClick(leftArrowPosition, motionEvent)){
                            handleMoving(false)
                            return true
                        }
                    }

                    //rightArrow
                    if (hook.currentPosition.right < screenWidth - screenWidth / 20) {
                        if(handlePossibleClick(rightArrowPosition, motionEvent)){
                            handleMoving(true)
                            return true
                        }
                    }

                    if(handlePossibleClick(buyButton, motionEvent)){
                        handleBuy()
                        return true
                    }

                    if(handlePossibleClick(reserveButton, motionEvent)){
                        return if(hook.endPosition.bottom > screenHeight/2)  {
                            handleUnreserve()
                            true
                        } else {
                            handleReserve()
                            true
                        }
                    }

                }
            }
        }
        return false
    }

    private fun handlePossibleClick (positionObject: RectF, positionClick: MotionEvent): Boolean {
        if(positionObject.left <= positionClick.x && positionObject.right > positionClick.x){
            if(positionObject.top <= positionClick.y && positionObject.bottom > positionClick.y){
                return true
            }
        }
        return false
    }

    private fun createFlippedBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f
        )
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}