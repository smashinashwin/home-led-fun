package com.leds.lightcontroller

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.*
import com.leds.lightcontroller.data.LightParams
import com.leds.lightcontroller.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mqttClient: MqttAndroidClientWrapper
    lateinit var lightParams: LightParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        val menu = binding.bottomAppBar
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupWithNavController(binding.bottomAppBar, navController)

        mqttClient = MqttAndroidClientWrapper(this)
        lightParams = LightParams()
        var powerStatus: Boolean = true

        binding.powerButton.setOnClickListener {
            powerStatus = flipSwitch(binding.powerButton, powerStatus)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mqttClient.isConnected()) mqttClient.connectMqtt()
        Log.i("MainActivityOnStart", "mqtt client created")
    }

    private fun flipSwitch(powerButton: FloatingActionButton, powerStatus: Boolean): Boolean {

        if (powerStatus) {
            mqttClient.send(stateOrPattern = 1, parameter = "state", value = 0)
            powerButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorOn))
            powerButton.setImageResource(R.drawable.ic_power)
        }
        else {
            powerButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorOff))
            powerButton.setImageResource(R.drawable.ic_outline_power_white)
            mqttClient.send(stateOrPattern = 1, parameter = "state", value = 1)
        }
         return !powerStatus
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }
}