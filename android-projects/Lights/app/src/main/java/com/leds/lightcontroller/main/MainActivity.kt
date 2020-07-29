package com.leds.lightcontroller.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.leds.lightcontroller.databinding.ActivityMainBinding
import com.leds.lightcontroller.R
import com.leds.lightcontroller.livedata.LightParams


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = ViewModelProvider(this)
        viewModel = model.get(MainViewModel::class.java)
        viewModel.initialize(this)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        val menu = binding.bottomAppBar
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupWithNavController(binding.bottomAppBar, navController)
        binding.mainViewModel = viewModel
        //TODO: is 'this' right?
        binding.lifecycleOwner = this

        val paramParamsObserver = Observer<String> {
            //this needs to handle everything that can be changed.
            //biggest outlier is the power button.
            viewModel.sendMessage(it)
        }
        viewModel.paramParams.mediator.observe(this, paramParamsObserver)
        binding.mainViewModel = this.viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.connectMqtt()
    }

    //this is a good place for live data. make the powerStatus observable. the onclick event changes the powerStatus in the
    //view model, and the observable should change the color of the button

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    fun flipSwitch(view: View) {
        viewModel.flipSwitch()
    }
}