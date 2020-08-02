package com.leds.lightcontroller.main

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.leds.lightcontroller.databinding.ActivityMainBinding
import com.leds.lightcontroller.R
import com.leds.lightcontroller.livedata.ParamParams
import com.leds.lightcontroller.ui.EmberFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //create the view model to be used by all views and fragments in the app.
        val model = ViewModelProvider(this)
        viewModel = model.get(MainViewModel::class.java)
        viewModel.initialize(this)

        //Load saved app data in case of navigating away or rotating.
        if (savedInstanceState != null) {
            viewModel.paramParams = savedInstanceState.get("key_live_data") as ParamParams
        }

        //draw the xml layout to the screen
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)

        //ensure the back button works correctly
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)

        //set up navigation in the bottom app bar
        val menu: BottomAppBar = binding.bottomAppBar
        menu.setOnMenuItemClickListener { menuItem ->
            navController.navigate(menuItem.itemId)
            true
        }


        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)

        //set data variable in activity_main.xml for data binding.
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this

        //super observer that watches paramParams.mediator.
        val paramParamsObserver = Observer<String> {
            viewModel.sendMessage(it)
        }
        viewModel.paramParams.mediator.observe(this, paramParamsObserver)
        binding.mainViewModel = this.viewModel
    }

    //save app data incase of rotate or background.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("key_live_data", viewModel.paramParams)
    }

    override fun onStart() {
        super.onStart()
        //ensure mqtt is connected.
        viewModel.connectMqtt()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    fun flipSwitch(view: View) {
        //this method is called from the FAB in activity_main.xml
        viewModel.flipSwitch()
    }
    fun saveSetting(view: View) {
        val navHost = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val currentFragment = navHost!!.childFragmentManager.primaryNavigationFragment
        val a = (currentFragment is EmberFragment)
        Log.i("ember fragment", a.toString())
        //based on what fragment, do a thing.
        //The mediator is an OK proxy, but won't work 100% of the time. 
        viewModel.saveSetting()
    }

}