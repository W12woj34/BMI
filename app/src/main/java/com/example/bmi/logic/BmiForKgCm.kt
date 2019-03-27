package com.example.bmi.logic

class BmiForKgCm(private var mass: Int, private var height : Int) : Bmi {

    private var bmi = mass * 10000.0 / (height*height)


    override fun countBmi(): Double {
        if((mass <=0 || mass > 999) && (height <=0 || height > 999)){
            return -3.0
        }
        if(mass <=0 || mass > 999){
            return -1.0
        }
        if(height <=0 || height > 999){
            return -2.0
        }
        return bmi
    }


}