package com.leds.lightcontroller.data

import java.io.File
import java.io.FileReader
import java.util.*

/*
 * Simple data class that loads res/raw/_mqttconfig.txt
 */
data class MqttParams (
    var password: CharArray,
    var username: String,
    var clientId: String,
    var stateTopic: String,
    var patternTopic: String,
    var lightTopic: String,
    var serverURL: String
)