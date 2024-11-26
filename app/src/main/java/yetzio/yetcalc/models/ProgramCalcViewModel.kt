package yetzio.yetcalc.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yetzio.yetcalc.enums.NumberSystem

class ProgramCalcViewModel: ViewModel(){
    var result: String = ""
    var numberSys: MutableLiveData<NumberSystem> = MutableLiveData(NumberSystem.DEC)

    var hexResult: String = ""
    var decResult: String = ""
    var octResult: String = ""
    var binResult: String = ""

    var init = false
    var hapticPref = false

}