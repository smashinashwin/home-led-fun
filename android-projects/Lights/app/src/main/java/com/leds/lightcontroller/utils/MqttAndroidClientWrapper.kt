package com.leds.lightcontroller.utils

import android.os.SystemClock.uptimeMillis
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leds.lightcontroller.data.MqttParams
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import com.google.gson.Gson
import com.leds.lightcontroller.main.MainActivity
import com.leds.lightcontroller.R
import com.leds.lightcontroller.data.MqttLightMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.concurrent.ConcurrentLinkedQueue


class MqttAndroidClientWrapper(activity: MainActivity): ViewModel() {
    init {
        loadParams(activity)
        connectMqtt(activity)
    }

    private var mainActivity: MainActivity = activity
    private lateinit var mqttParams: MqttParams
    private lateinit var mqttClient: MqttAndroidClient
    private var lastSend: Long = uptimeMillis()
    private val sendInterval = 50
    private var lightMessageQueue: ConcurrentLinkedQueue<MqttLightMessage> = ConcurrentLinkedQueue()

    private fun loadParams(activity: MainActivity) {
        var reader: InputStream = activity.resources.openRawResource(R.raw._mqttconfig)
        var props = Properties()
        props.load(reader)
        val pwd = props["password"] as String
        this.mqttParams = MqttParams(
            password = pwd.toCharArray(),
            username = props["username"] as String,
            clientId = props["clientId"] as String,
            stateTopic = props["stateTopic"] as String,
            patternTopic = props["patternTopic"] as String,
            lightTopic = props["lightTopic"] as String,
            serverURL = props["serverURL"] as String
        )
    }

    fun isConnected(): Boolean {
        return mqttClient.isConnected
    }
    fun connectMqtt(activity: MainActivity =this.mainActivity) {
        val mqttOptions = MqttConnectOptions()
        mqttOptions.password = mqttParams.password
        mqttOptions.userName = mqttParams.username
        this.mqttClient = MqttAndroidClient(activity.applicationContext, mqttParams.serverURL,mqttParams.clientId)
        try {
            val token: IMqttToken = mqttClient.connect(mqttOptions)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(AsyncActionToken: IMqttToken) {
                    Log.i("connection ", "success")
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.i("connection", "failed", exception)
                }
            }
        }

        catch (e: Exception) {
            Log.i("exception", "connection exception")
            e.printStackTrace()
        }
    }

    fun send(lightTopic: String=mqttParams.lightTopic, stateOrPattern: Int, parameter: String, value: Any) {
        val msg = MqttLightMessage(lightTopic = lightTopic,
            stateOrPattern = stateOrPattern,
            parameter = parameter,
            value = value
        )
        val queueIsEmpty: Boolean = lightMessageQueue.isEmpty()
        lightMessageQueue.add(msg)
        if (queueIsEmpty) sendFromSendQueue()
        Log.i("added to queue", lightMessageQueue.size.toString())
    }

    private fun sendFromSendQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            while (!lightMessageQueue.isEmpty()) {
                if (uptimeMillis() - lastSend > sendInterval) {
                    //avoid collisions
                    if (!lightMessageQueue.isEmpty()) sendMessage(lightMessageQueue.remove())
                    lastSend = uptimeMillis()
                    Log.i("removed from queue", lightMessageQueue.size.toString())
                }
            }
        }
    }

    private fun sendMessage(msg: MqttLightMessage) {
        val gson = Gson()
        val functionTopic: String = when (msg.stateOrPattern) {
            0 -> mqttParams.patternTopic
            else -> mqttParams.stateTopic
        }
        val topic = "${msg.lightTopic}/$functionTopic"
        val payloadMap: Map<String, Any> = mapOf(msg.parameter to msg.value)
        val payloadString: String = gson.toJson(payloadMap)
        val mqttMsg = MqttMessage()
        mqttMsg.payload = payloadString.toByteArray()
        if (mqttClient.isConnected) {
            try {
                mqttClient.publish(topic, mqttMsg)
                Log.i("mqttsendSuccess", payloadString)
            }
            catch (e: java.lang.Exception) {
                Log.i("error sending mqtt", "exception: $e \npayload: $payloadString")
            }
        }
        else {
            Log.i("mqttSendFail", payloadString)
        }
    }
}