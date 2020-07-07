package com.leds.lightcontroller.data

data class EmberParams(
    var emberDelayMin: Int = 0,
    var emberDelayMax: Int = 0,
    var emberBrightenMin: Int = 1,
    var emberBrightenMax: Int = 5,
    var emberDimMin: Int = 1,
    var emberDimMax: Int = 5,
    var emberBrightnessTriggerMin: Int = 220,
    var emberBrightnessTriggerMax: Int = 255

)