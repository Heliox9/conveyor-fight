package de.conveyorfight.fight

import android.content.Context
import android.graphics.*
import android.graphics.drawable.AnimatedImageDrawable
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import android.view.SurfaceView
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import de.conveyorfight.assets.Item
import java.util.*
import kotlin.math.ceil



class FightView(
    context: Context,
    private val isPlayerFirst: Boolean,
    private val playerCharacter: Character,
    private val playerCharacterAfterDamage: Character,
    private val enemyCharacter: Character,
    private val enemyCharacterAfterDamage: Character,
    val handleGameEnd: () -> Unit,
    val handleRoundEnd: () -> Unit
) : SurfaceView(context), SurfaceHolder.Callback {

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()
    private val size = DisplayMetrics()
    private var textSize: Float = 0F
    private var heightUnit: Int = 0
    private var victoryGif: AnimatedImageDrawable = AnimatedImageDrawable()
    private var defeatGif: AnimatedImageDrawable = AnimatedImageDrawable()

    var characterBitMap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.character_red_hair)

    var background: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background_fight)

    var characterSplash: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.charakter_splash)

    var tile: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.tile)

    private fun init () {
        display?.apply {
            getRealMetrics(size)
        }

        background = createFullScreenBitmap(background)
        characterSplash = createFullScreenBitmap(characterSplash)

        heightUnit = size.heightPixels / 8
        tile = Bitmap.createScaledBitmap(tile, heightUnit, heightUnit, false)

        val victorySource = ImageDecoder.createSource(context.resources, R.raw.victory)
        victoryGif = ImageDecoder.decodeDrawable(victorySource) as AnimatedImageDrawable

        val defeatSource = ImageDecoder.createSource(context.resources, R.raw.defeat)
        defeatGif = ImageDecoder.decodeDrawable(defeatSource) as AnimatedImageDrawable

        textSize = (size.heightPixels/30).toFloat()
    }

    fun start () {
        holder.addCallback(this)
    }

    private fun createFullScreenBitmap (bitmap: Bitmap): Bitmap {
        val screenWidth: Double = size.widthPixels.toDouble()
        val screenHeight: Double = size.heightPixels.toDouble()
        val bitmapWidth: Double = bitmap.width.toDouble()
        val bitmapHeight: Double = bitmap.height.toDouble()
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

        return Bitmap.createScaledBitmap(bitmap, backgroundWidth, backgroundHeight, true)
    }

    private fun drawScene() {
        init()

        canvas = holder.lockCanvas()
        canvas.drawBitmap(background, 0F, 0F, null)
        drawCharacter(playerCharacter, true)
        drawCharacter(enemyCharacter, false)
        drawItems()

        holder.unlockCanvasAndPost(canvas)

        Thread.sleep(3000)

        canvas = holder.lockCanvas()
        canvas.drawBitmap(background, 0F, 0F, null)
        drawCharacter(playerCharacter, true)
        drawCharacter(enemyCharacter, false)
        drawItems()
        drawAttack(isPlayerFirst)
        holder.unlockCanvasAndPost(canvas)

        Thread.sleep(1000)

        canvas = holder.lockCanvas()
        canvas.drawBitmap(background, 0F, 0F, null)
        drawCharacter(if(isPlayerFirst) playerCharacter else playerCharacterAfterDamage, true)
        drawCharacter(if(isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter, false)
        drawItems()
        holder.unlockCanvasAndPost(canvas)

        Thread.sleep(3000)

        if(isPlayerFirst) {
            if (enemyCharacterAfterDamage.hp <= 0) {
                canvas = holder.lockCanvas()
                canvas.drawBitmap(background, 0F, 0F, null)
                drawCharacter(
                    if (isPlayerFirst) playerCharacter else playerCharacterAfterDamage,
                    true
                )
                drawCharacter(
                    if (isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter,
                    false
                )
                drawItems()
                handleWin()
                return
            }
        } else {
            if (playerCharacterAfterDamage.hp <= 0){
                canvas = holder.lockCanvas()
                canvas.drawBitmap(background, 0F, 0F, null)
                drawCharacter(
                    if (isPlayerFirst) playerCharacter else playerCharacterAfterDamage,
                    true
                )
                drawCharacter(
                    if (isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter,
                    false
                )
                drawItems()
                handleLoose()
                return
            }
        }

        canvas = holder.lockCanvas()
        canvas.drawBitmap(background, 0F, 0F, null)
        drawCharacter(if(isPlayerFirst) playerCharacter else playerCharacterAfterDamage, true)
        drawCharacter(if(isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter, false)
        drawItems()
        drawAttack(!isPlayerFirst)
        holder.unlockCanvasAndPost(canvas)

        Thread.sleep(1000)

        canvas = holder.lockCanvas()
        canvas.drawBitmap(background, 0F, 0F, null)
        drawCharacter(playerCharacterAfterDamage, true)
        drawCharacter(enemyCharacterAfterDamage, false)
        drawItems()
        holder.unlockCanvasAndPost(canvas)

        Thread.sleep(3000)

        if (isPlayerFirst) {
            if (playerCharacterAfterDamage.hp <= 0){
                canvas = holder.lockCanvas()
                canvas.drawBitmap(background, 0F, 0F, null)
                drawCharacter(
                    if (isPlayerFirst) playerCharacter else playerCharacterAfterDamage,
                    true
                )
                drawCharacter(
                    if (isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter,
                    false
                )
                drawItems()
                handleLoose()
                return
            }
        } else {
            if (enemyCharacterAfterDamage.hp <= 0) {
                canvas = holder.lockCanvas()
                canvas.drawBitmap(background, 0F, 0F, null)
                drawCharacter(
                    if (isPlayerFirst) playerCharacter else playerCharacterAfterDamage,
                    true
                )
                drawCharacter(
                    if (isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter,
                    false
                )
                drawItems()
                handleWin()
                return
            }
        }
        handleRoundEnd()
    }

    private fun drawItems() {
        //items
        val screenWidth = size.widthPixels
        val spaceUnit = heightUnit.toFloat()
        val firstRowTop = spaceUnit * 2
        val secondRowTop = spaceUnit * 4
        val thirdRowTop = spaceUnit * 3

        //helmet
        drawTile(playerCharacter.helmet, spaceUnit, firstRowTop, true)
        drawTile(enemyCharacter.helmet, screenWidth - (spaceUnit + heightUnit), firstRowTop, false)

        //armor
        drawTile(playerCharacter.armor, 3 * spaceUnit, firstRowTop, true)
        drawTile(enemyCharacter.armor, screenWidth - (3 * spaceUnit + heightUnit), firstRowTop, false)

        //gloves
        drawTile(playerCharacter.gloves, 5 * spaceUnit, firstRowTop, true)
        drawTile(enemyCharacter.gloves, screenWidth - (5 * spaceUnit + heightUnit), firstRowTop, false)

        //pants
        drawTile(playerCharacter.pants, spaceUnit, secondRowTop, true)
        drawTile(enemyCharacter.pants, screenWidth - (spaceUnit + heightUnit), secondRowTop, false)

        //shoes
        drawTile(playerCharacter.shoes, 3 * spaceUnit, secondRowTop, true)
        drawTile(enemyCharacter.shoes, screenWidth - (3 * spaceUnit + heightUnit), secondRowTop, false)

        //special
        drawTile(playerCharacter.special, 5 * spaceUnit, secondRowTop, true)
        drawTile(enemyCharacter.special, screenWidth - (5 * spaceUnit + heightUnit), secondRowTop, false)

        //weapon
        drawTile(playerCharacter.weapon, 7 * spaceUnit, thirdRowTop, true)
        drawTile(enemyCharacter.weapon, screenWidth - (7 * spaceUnit + heightUnit), thirdRowTop, false)

    }

    private fun drawTile(item: Item?, xPosition: Float, yPosition: Float, isPlayer: Boolean) {

        canvas.drawBitmap(tile, xPosition, yPosition, null)
        if (item != null){
            val itemBitmap = Bitmap.createScaledBitmap(item.bitmap, heightUnit, heightUnit, false)
            canvas.drawBitmap(itemBitmap, xPosition, yPosition, null)
            if(isPlayer){
                var i = 0
                while (i < item.properties.size) {
                    val currentProperty = item.properties[i]
                    paint.textSize = textSize
                    paint.color = Color.argb(255, 255, 255, 255)
                    val text = currentProperty.property.name.replace("_", " ")
                        .lowercase(Locale.getDefault())
                        .replaceFirstChar { it.uppercase() }
                    canvas.drawText("${text}: ${currentProperty.value}",
                        xPosition,
                        yPosition - ((textSize + 4) *(i + 1)),
                        paint)
                    i++
                }
            }
        }
    }

    private fun drawCharacter(character: Character, isPlayer: Boolean){
        val screenWidth = size.widthPixels.toDouble()
        val screenHeight = size.heightPixels.toDouble()

        //Character
        var characterBitMap = Bitmap.createScaledBitmap(characterBitMap, heightUnit, heightUnit, false)
        if(!isPlayer){
            characterBitMap = createFlippedBitmap(characterBitMap)
        }
        val characterPosition = RectF(
            if(isPlayer) (heightUnit*2).toFloat() else (screenWidth - heightUnit*3).toFloat(),
            (screenHeight - 2*heightUnit).toFloat(),
            if(isPlayer) (heightUnit*3).toFloat() else (screenWidth - heightUnit*2).toFloat(),
            (screenHeight - heightUnit).toFloat()
        )
        canvas.drawBitmap(characterBitMap, null, characterPosition, null)

        //HP
        val hpBarLength: Float = (heightUnit * (maxOf(character.hp.toDouble(), 0.0) / 100)).toFloat()
        paint.color = Color.argb(255, 0, 240, 0)
        canvas.drawRect(
            characterPosition.left,
            characterPosition.top - heightUnit/4,
            characterPosition.left + hpBarLength,
            characterPosition.top - heightUnit/2, paint)
        paint.color = Color.argb(255, 100, 100, 100)
        canvas.drawRect(
            characterPosition.left + hpBarLength,
            characterPosition.top - heightUnit/4,
            characterPosition.right,
            characterPosition.top - heightUnit/2, paint)
        paint.color = Color.argb(255, 255, 255, 255)
        paint.textSize = textSize
        canvas.drawText("${character.hp} \\ 100", characterPosition.left, characterPosition.top - heightUnit/2, paint)
    }

    private fun drawAttack(isPlayer: Boolean) {
        var characterSplashScreen = characterSplash
        if(!isPlayer) {
            characterSplashScreen = createFlippedBitmap(characterSplashScreen)
        }
        canvas.drawBitmap(characterSplashScreen,0f, 0f, null)
    }

    private fun handleWin() {
        victoryGif
        victoryGif.start()
        victoryGif.draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        handleGameEnd()
        Thread.sleep(5000)
    }

    private fun handleLoose() {
        defeatGif.draw(canvas)
        defeatGif.start()
        holder.unlockCanvasAndPost(canvas)
        handleGameEnd()
        Thread.sleep( 5000)
    }

    private fun createFlippedBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f
        )
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawScene()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // nothing to do here
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        //nothing to do here
    }
}