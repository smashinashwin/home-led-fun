package com.leds.lightcontroller.data

data class MqttLightMessage (
    var lightTopic: String = "lamp",
    var stateOrPattern: Int,
    var parameter: String,
    var value: Any
)