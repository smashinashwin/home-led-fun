package com.example.lights.data

data class MqttParams (
    var password: CharArray = "redacted".toCharArray(),
    var username: String = "bashwin",
    var clientId: String = "lightApp",
    var stateTopic: String = "state",
    var patternTopic: String = "pattern",
    var lightTopic: String = "lamp",
    var serverURL: String = "tcp://"also_redacted""

)