package com.leds.lightcontroller.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.leds.lightcontroller.databinding.ActivityMainBinding
import com.leds.lightcontroller.R


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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

        val menu: BottomAppBar = binding.bottomAppBar

        menu.setOnMenuItemClickListener { menuItem ->
            navController.navigate(menuItem.itemId)
            true
        }

        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this

        val paramParamsObserver = Observer<String> {
            viewModel.sendMessage(it)
        }
        viewModel.paramParams.mediator.observe(this, paramParamsObserver)
        binding.mainViewModel = this.viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.connectMqtt()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    fun flipSwitch(view: View) {
        viewModel.flipSwitch()
    }
}