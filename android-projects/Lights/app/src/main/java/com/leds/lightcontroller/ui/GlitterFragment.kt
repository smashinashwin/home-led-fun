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
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(mainActivity)
        viewModel = model.get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_glitter, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val lightParams = mainActivity.viewModel.paramParams.lightParams
        lightParams.propertyMap["pattern"]!!.value = getString(R.string.glitterpatternnumber)
    }
}


