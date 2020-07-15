package com.leds.lightcontroller

import android.os.SystemClock
import android.util.Log
import com.leds.lightcontroller.data.MqttParams
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import com.google.gson.Gson
import java.io.InputStream


class MqttAndroidClientWrapper(activity: MainActivity) {
    init {
        loadParams(activity)
        connectMqtt(activity)
    }

    private lateinit var mqttParams: MqttParams
    private lateinit var mqttClient: MqttAndroidClient
    private var lastSend: Long = SystemClock.uptimeMillis()
    private val sendInterval = 50

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

    private fun connectMqtt(activity: MainActivity) {
        val mqttOptions = MqttConnectOptions()
        mqttOptions.password = mqttParams.password
        Log.i("mqttPass", mqttParams.password.toString())
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
        if (SystemClock.uptimeMillis() - lastSend > sendInterval) {
            val gson = Gson()
            val functionTopic: String = when (stateOrPattern) {
                0 -> mqttParams.patternTopic
                else -> mqttParams.stateTopic
            }

            val topic = "$lightTopic/$functionTopic"
            val payloadMap = mapOf(parameter to value)
            val payloadString: String = gson.toJson(payloadMap)
            Log.i("payload", payloadString)
            val msg = MqttMessage()
            msg.payload = payloadString.toByteArray()
            if (mqttClient.isConnected) {
                mqttClient.publish(topic, msg)
                Log.i("mqttsend", "successful")
            }
            else Log.i("mqttsend", "unsuccessful")
            lastSend = SystemClock.uptimeMillis()
        }
    }
}