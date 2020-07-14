package com.leds.lightcontroller.data

data class SolidParams(
    var solidColorRed: Int = 255,
    var solidColorGreen: Int = 50,
    var solidColorBlue: Int = 155,
    var solidColorWhite: Int = 255,
    var brightness: Int = 255,
    var color: Int = (255 and 0xff) shl 24 or (solidColorRed and 0xff) shl 16 or (solidColorGreen and 0xff) shl 8 or (solidColorBlue shl 0xff)
)
