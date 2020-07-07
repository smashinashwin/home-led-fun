package com.example.lights.data

import android.icu.number.IntegerWidth

data class GlitterParams(
    var chanceOfGlitter: Int = 200,
    var glitterDimDelayMin: Int = 15,
    var glitterDimDelayMax: Int = 4,
    var glitterDimMin: Int = 3,
    var glitterDimMax: Int = 50,
    var glitterBrightenDelayMin: Int = 0,
    var glitterBrightenDelayMax: Int = 0

)

