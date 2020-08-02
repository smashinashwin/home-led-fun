package com.leds.lightcontroller.data

/*
 *  Simple data class to wrap messages to send to the lamp.
 */
data class MqttLightMessage (
    var lightTopic: String = "lamp",
    var stateOrPattern: Int,
    var parameter: String,
    var value: Any


) {
    override fun toString(): String {
        return "($parameter, $value)"
    }
}