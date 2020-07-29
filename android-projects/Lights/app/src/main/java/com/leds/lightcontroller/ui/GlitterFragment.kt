package com.leds.lightcontroller.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leds.lightcontroller.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.leds.lightcontroller.main.MainActivity
import com.leds.lightcontroller.databinding.FragmentGlitterBinding
import com.leds.lightcontroller.main.MainViewModel


class GlitterFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentGlitterBinding
    lateinit var mainActivity: MainActivity
    //lateinit var selectorFragment: SelectorFragment

    //data binding variables from the EmberParams class. https://classroom.udacity.com/courses/ud9012/lessons/4f6d781c-3803-4cb9-b08b-8b5bcc318d1c/concepts/68f0a220-8b33-43fa-be12-1110578a0e1b
    //use mqtt to get them.
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //this is where you want to mqtt to the light to activate the ember fragment
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(mainActivity)
        viewModel = model.get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_glitter, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        /*
        val glitterObserver = Observer<GlitterParams> {
            viewModel.setGlitterParams(mainActivity.viewModel.mqttClient, mainActivity.viewModel.lightParams)
        }


        viewModel.glitterParams.observe(viewLifecycleOwner, glitterObserver)
        */
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val lightParams = mainActivity.viewModel.paramParams.lightParams
        lightParams.propertyMap["pattern"]!!.value = getString(R.string.glitterpatternnumber)
    }
}


