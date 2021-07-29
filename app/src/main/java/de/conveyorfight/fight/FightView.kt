package de.conveyorfight.fight

import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.view.SurfaceView
import de.conveyorfight.R
import de.conveyorfight.assets.Character
import kotlin.math.ceil

// TODO: aus clonen companion objekte erstellen

class FightView(
    context: Context,
    val isPlayerFirst: Boolean,
    val playerCharacter: Character,
    val playerCharacterAfterDamage: Character,
    val enemyCharacter: Character,
    val enemyCharacterAfterDamage: Character,
    val initiateWin: () -> Unit,
    val initiateLoose: () -> Unit
) : SurfaceView(context) {

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()
    private val size = DisplayMetrics()

    var background: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background_fight)

    init {
        //TODO: richtige Metrics bekommen
        display?.apply {
            getRealMetrics(size)
        }
        getBackgroundBitmap()
    }


    private fun getBackgroundBitmap () {
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

    fun drawScene() {
        while (true){
            if (!holder.surface.isValid){
                continue
            }

            canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0F, 0F, null)
            drawCharacterAndItems(playerCharacter, true)
            drawCharacterAndItems(enemyCharacter, false)
            holder.unlockCanvasAndPost(canvas)

            Thread.sleep(3000)

            canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0F, 0F, null)
            drawCharacterAndItems(playerCharacter, true)
            drawCharacterAndItems(enemyCharacter, false)
            drawAttack(isPlayerFirst)
            holder.unlockCanvasAndPost(canvas)

            Thread.sleep(1000)

            canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0F, 0F, null)
            drawCharacterAndItems(if(isPlayerFirst) playerCharacter else playerCharacterAfterDamage, true)
            drawCharacterAndItems(if(isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter, false)
            holder.unlockCanvasAndPost(canvas)

            Thread.sleep(3000)

            if(enemyCharacter.hp <= 0 || playerCharacter.hp <= 0){
                canvas = holder.lockCanvas()
                canvas.drawBitmap(background, 0F, 0F, null)
                drawCharacterAndItems(if(isPlayerFirst) playerCharacter else playerCharacterAfterDamage, true)
                drawCharacterAndItems(if(isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter, false)

                if (isPlayerFirst){
                    handleWin()
                } else {
                    handleLoose()
                }
                holder.unlockCanvasAndPost(canvas)
                break
            }

            canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0F, 0F, null)
            drawCharacterAndItems(if(isPlayerFirst) playerCharacter else playerCharacterAfterDamage, true)
            drawCharacterAndItems(if(isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter, false)
            drawAttack(!isPlayerFirst)
            holder.unlockCanvasAndPost(canvas)

            Thread.sleep(1000)

            canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0F, 0F, null)
            drawCharacterAndItems(playerCharacterAfterDamage, true)
            drawCharacterAndItems(enemyCharacterAfterDamage, false)
            holder.unlockCanvasAndPost(canvas)

            Thread.sleep(3000)

            if(enemyCharacter.hp <= 0 || playerCharacter.hp <= 0){
                canvas = holder.lockCanvas()
                canvas.drawBitmap(background, 0F, 0F, null)
                drawCharacterAndItems(if(isPlayerFirst) playerCharacter else playerCharacterAfterDamage, true)
                drawCharacterAndItems(if(isPlayerFirst) enemyCharacterAfterDamage else enemyCharacter, false)

                if (!isPlayerFirst){
                    handleWin()
                } else {
                    handleLoose()
                }
                holder.unlockCanvasAndPost(canvas)
            }
            break
        }
    }

    private fun drawCharacterAndItems(character: Character, isPlayer: Boolean){
        //TODO
    }

    private fun drawAttack(isPlayer: Boolean) {
        //TODO
    }

    private fun handleWin() {
        //TODO
    }

    private fun handleLoose() {
        //TODO
    }

    private fun createFlippedBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f
        )
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}