package com.leds.lightcontroller

import android.content.res.ColorStateList
import android.os.Bundle
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

        lightParams = LightParams()
        var powerStatus: Boolean = true
        mqttClient = MqttAndroidClientWrapper(this)

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
        }
        else {
            //Toast.makeText(this, "power on", Toast.LENGTH_SHORT).show()
            //powerButton.setBackgroundColor(this.getColor(R.color.colorOn))
            powerButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorOff))
            powerButton.setImageResource(R.drawable.ic_outline_power_white)
        }
         return !powerStatus
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }
}