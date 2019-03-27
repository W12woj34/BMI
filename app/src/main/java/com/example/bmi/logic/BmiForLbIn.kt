package com.example.bmi.logic

class BmiForLbIn(private var mass: Int, private var height : Int) : Bmi {
    private var bmi = mass * 703.0 / (height * height)


    override fun countBmi(): Double {
        return bmi
    }

}