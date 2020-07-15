package com.leds.lightcontroller.data

import android.graphics.Color
import android.graphics.Color.rgb
import java.lang.Integer.toHexString

data class SolidParams(
    var solidColorRed: Int = 255,
    var solidColorGreen: Int = 50,
    var solidColorBlue: Int = 155,
    var solidColorWhite: Int = 255,
    var brightness: Int = 255,
    var color: Int = rgb(solidColorRed, solidColorGreen, solidColorBlue),
    var colorStr: String = "#" + toHexString(color)

)
