package com.example.bmi.logic

class BmiForKgCm(private var mass: Int, private var height : Int) : Bmi {
    private var bmi = mass * 10000.0 / (height*height)


    override fun countBmi(): Double {
        return bmi
    }


}