package com.leds.lightcontroller.ui.solid

import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.leds.lightcontroller.MainActivity
import com.leds.lightcontroller.R
import com.leds.lightcontroller.databinding.FragmentEmberBinding
import com.leds.lightcontroller.databinding.FragmentSolidBinding
import com.leds.lightcontroller.ui.ember.EmberViewModel

class SolidFragment : Fragment() {

    private lateinit var viewModel: SolidViewModel
    private lateinit var binding: FragmentSolidBinding
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(this)
        viewModel = model.get(SolidViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solid, container, false)
        binding.solidParams = viewModel.solidParams
        Log.i("color",  viewModel.solidParams.color.toString())
        Log.i("color Red", Color.red(viewModel.solidParams.color).toString())
        //binding.textSolid.setBackgroundColor(viewModel.solidParams.color)


        return binding.root
    }
}