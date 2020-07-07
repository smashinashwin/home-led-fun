package com.example.lights

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.lights.data.LightParams
import com.example.lights.data.MqttParams
import com.example.lights.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_pattern.view.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mqttClient: MqttAndroidClient
    lateinit var lightParams: LightParams
    lateinit var mqttParams: MqttParams
    //lateinit var navView: BottomAppBar


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("test", "test")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        val menu = binding.bottomAppBar
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupWithNavController(binding.bottomAppBar, navController)
        lightParams = LightParams()
        mqttClient = connectMqtt()
        var powerStatus: Boolean = true

        binding.powerButton.setOnClickListener {
            powerStatus = flipSwitch(binding.powerButton, powerStatus)
        }
    }

    private fun connectMqtt(): MqttAndroidClient {
        mqttParams = MqttParams()
        val mqttOptions = MqttConnectOptions()
        mqttOptions.password = mqttParams.password
        mqttOptions.userName = mqttParams.username
        mqttClient = MqttAndroidClient(this,mqttParams.serverURL,mqttParams.clientId)
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
        return mqttClient

    }
    private fun flipSwitch(powerButton: FloatingActionButton, powerStatus: Boolean): Boolean {
        val msg = MqttMessage()
        msg.payload = "mur".toByteArray()
        mqttClient.publish("lamp/state", msg)

        if (powerStatus) {
            //Toast.makeText(this, "power off", Toast.LENGTH_SHORT).show()
            //powerButton.setBackgroundColor(this.getColor(R.color.colorOff))
            powerButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorOn))
            powerButton.setImageResource(R.drawable.ic_power)
            Log.i("power", "off")

        }
        else {
            //Toast.makeText(this, "power on", Toast.LENGTH_SHORT).show()
            //powerButton.setBackgroundColor(this.getColor(R.color.colorOn))
            powerButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorOff))
            powerButton.setImageResource(R.drawable.ic_outline_power_white)
            Log.i("power", "on")
        }
         return !powerStatus
    }

    fun test() {
        Log.i("mainActivity", "we did it")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    fun updatePalette(palette: String) {
        val msg = MqttMessage()
        val payloadString = "{\"palette\":\"$palette\"}"
        msg.payload = payloadString.toByteArray()
        val topic = "${mqttParams.lightTopic}/${mqttParams.patternTopic}"
        if (mqttClient.isConnected()) {
            mqttClient.publish(topic, msg)
            Log.i("palettechange", "sent successfully")
        }
    }

    fun updatePattern(pattern: Int) {
        val msg = MqttMessage()
        val payloadString = "{\"pattern\":$pattern}"
        msg.payload = payloadString.toByteArray()
        val topic = "${mqttParams.lightTopic}/${mqttParams.patternTopic}"
        if (mqttClient.isConnected()) {
            mqttClient.publish(topic, msg)
            Log.i("patternchange", "sent successfully")
        }
        Log.i("patternchange", "unsuccessful")

    }

}