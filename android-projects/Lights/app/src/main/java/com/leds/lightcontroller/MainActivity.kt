package com.leds.lightcontroller

import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.*
import com.leds.lightcontroller.data.LightParams
import com.leds.lightcontroller.data.MqttParams
import com.leds.lightcontroller.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.FileReader
import java.io.InputStream
import java.security.AccessController.getContext
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mqttClient: MqttAndroidClientWrapper
    lateinit var lightParams: LightParams
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
        var powerStatus: Boolean = true
        mqttClient = MqttAndroidClientWrapper(this)
        //this isn't working because you need the context to create an mqttandroidclient
        //will an mqtt client work? M<aybe


        binding.powerButton.setOnClickListener {
            powerStatus = flipSwitch(binding.powerButton, powerStatus)
        }
    }

    private fun flipSwitch(powerButton: FloatingActionButton, powerStatus: Boolean): Boolean {
        mqttClient.send(stateOrPattern = 1, parameter = "switch", value = "flip")
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
}