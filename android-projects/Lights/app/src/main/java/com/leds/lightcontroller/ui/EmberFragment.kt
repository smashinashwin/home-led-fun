package com.leds.lightcontroller.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leds.lightcontroller.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.leds.lightcontroller.main.MainActivity
import com.leds.lightcontroller.databinding.FragmentEmberBinding
import com.leds.lightcontroller.main.MainViewModel


class EmberFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentEmberBinding
    lateinit var mainActivity: MainActivity
    //lateinit var selectorFragment: SelectorFragment

    //data binding variables from the EmberParams class. https://classroom.udacity.com/courses/ud9012/lessons/4f6d781c-3803-4cb9-b08b-8b5bcc318d1c/concepts/68f0a220-8b33-43fa-be12-1110578a0e1b
    //use mqtt to get them.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(mainActivity)
        viewModel = model.get(MainViewModel::class.java)
        Log.i("ember view model", viewModel.toString())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ember, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //TODO: set the pattern livedata instead of setting the pattern here. that should kick off an event that sets the pattern
        val lightParams = mainActivity.viewModel.paramParams.lightParams
        lightParams.propertyMap["pattern"]!!.value = getString(R.string.emberpatternnumber)
    }

}


