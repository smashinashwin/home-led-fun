package com.leds.lightcontroller.data



data class LightParams(
    var stateOn: Boolean = false,
    var palette: String = "allStars",
    var pattern: Int = 0
)