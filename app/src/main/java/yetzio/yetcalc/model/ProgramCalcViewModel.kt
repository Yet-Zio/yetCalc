package yetzio.yetcalc.model

import androidx.lifecycle.ViewModel
import yetzio.yetcalc.component.NumberSystem
import yetzio.yetcalc.component.Operator

class ProgramCalcViewModel: ViewModel(){
    var initialized = false
    var textEXP = ""
    var textRES = ""
    var numberSys: NumberSystem = NumberSystem.DEC
    var currentOp: Operator? = null
    var isCalcPending = false
    var prevResult = 0
    var clearInput = false
}