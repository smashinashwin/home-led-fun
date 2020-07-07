package com.leds.lightcontroller.data



data class LightParams(
    var lightTopic: String = "lamp",
    var stateOn: Boolean = false,
    var palette: String = "allStars",
    var pattern: Int = 0
)