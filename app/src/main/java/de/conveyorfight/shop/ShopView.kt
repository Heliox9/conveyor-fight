package de.conveyorfight.shop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.SurfaceView
import de.conveyorfight.assets.Item

class ShopView (context: Context,
                val shopItems: List<Item>)
    : SurfaceView(context), Runnable{

    // A Canvas and a Paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()


    override fun run() {
        TODO("background malen")
        TODO("bestimme fps und mache while loop")
        TODO("Zeichne alle Items")
        TODO("Mache kaufen möglich")
        TODO("zugreifen auf wie viel Zeit übrig bleibt")
    }
}