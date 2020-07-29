package com.leds.lightcontroller.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leds.lightcontroller.main.MainActivity
import com.leds.lightcontroller.R
import com.leds.lightcontroller.databinding.FragmentSolidBinding
import com.leds.lightcontroller.main.MainViewModel

class SolidFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentSolidBinding
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(mainActivity)
        viewModel = model.get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solid, container, false)

        //live data in fragment_solid.xml
        //seek bars are two-way bound, meaning that their updates should already update the solidParams variable in the model
        //then the observer here triggers an update sent.
        //i think this means we don't need listeners anymore
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        /*

        val colorObserver = Observer<SolidParams> {
            viewModel.setSolidParams(mainActivity.viewModel.mqttClient, mainActivity.viewModel.lightParams)
            binding.textSolid.setBackgroundColor(viewModel.solidParams.value!!.color)
        }

        viewModel.solidParams.observe(viewLifecycleOwner, colorObserver)
        */

        return binding.root
    }

    //whenever this fragment is started, set the pattern to solid
    override fun onStart() {
        super.onStart()
        val lightParams = mainActivity.viewModel.paramParams.lightParams
        lightParams.propertyMap["pattern"]!!.value = getString(R.string.solidpatternnumber)
    }
}