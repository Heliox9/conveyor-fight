package de.conveyorfight

import android.app.Application

class ConveyorApplication : Application() {
    val volumeMusicMax: Int = 100
    val volumeEffectMax: Int = 100
    var volumeMusic: Int = volumeMusicMax / 5
    var volumeEffect: Int = volumeEffectMax / 5
}