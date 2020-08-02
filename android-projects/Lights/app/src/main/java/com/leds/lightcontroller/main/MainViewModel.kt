package com.leds.lightcontroller.main

import android.text.PrecomputedText
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.livedata.*
import com.leds.lightcontroller.utils.MqttAndroidClientWrapper

/*
 *  This is currently the only view model. It holds the MQTT wrapper object, and all live data params.
 *  It handles communication between the view and the MQTT wrapper.
 */
class MainViewModel : ViewModel() {

    lateinit var mqttClient: MqttAndroidClientWrapper
    lateinit var paramParams: ParamParams

    fun initialize(activity: MainActivity) {
        mqttClient = MqttAndroidClientWrapper(activity)
        paramParams = ParamParams()
    }

    fun connectMqtt() {
        if (!mqttClient.isConnected()) mqttClient.connectMqtt()
    }

    //TODO: convert all hard-coded strings to constants

    //based on paramParams.mediator, drill through its member variables to find the parameter and value that changed.
    //send these to the lamp.
    fun sendMessage(propertyName: String) {
        //choose the glitter or ember object, find the changed parameter and value, and send.
        val property: ParamsLiveData? = paramParams.propertyMap[propertyName]
        val parameter: String = property!!.mediator.value!!
        val lightParams = paramParams.lightParams
        val lightTopic = lightParams.propertyMap["lightTopic"]!!.value!!

        if (property is IntParamsLiveData) {
            val parameterValue: Int = property.propertyMap[parameter]!!.value!!
            mqttClient.send(lightTopic, 0, parameter, parameterValue)
        }
        else if (property is StringParamsLiveData){
            val parameterValue: String = property.propertyMap[parameter]!!.value!!
            var stateOrPattern = 0
            if (parameter == "stateOn") stateOrPattern = 1
            mqttClient.send(lightTopic, stateOrPattern, parameter, parameterValue)
        }
    }

    //turn the lamp on or off.
    fun flipSwitch() {
        val lightParams = paramParams.lightParams
        val powerStatus = lightParams.propertyMap["stateOn"]!!.value == "false"
        //This value just changes the LiveData<String> object lightParams.stateOn. The paramParamsObserver then kicks off viewModel.sendMessage()
        lightParams.propertyMap["stateOn"]!!.value = if (powerStatus)  "true" else "false"
    }

    fun saveSetting() {
        Log.i("setting to be saved", paramParams.mediator.value!!)
        //TODO("add current settings to the postgres database on the rpi ")
        //https://stackoverflow.com/questions/10435609/driver-jdbc-postgresql-with-android

    }
}