package yetzio.yetcalc.models

import androidx.lifecycle.ViewModel
import yetzio.yetcalc.enums.AngleMode
import yetzio.yetcalc.enums.CalcMode

class CalcViewModel: ViewModel(){
    var calcMode: CalcMode = CalcMode.FIRSTMODE
    var angleMode: AngleMode = AngleMode.DEGREE
    var lastSetFactBt: CalcMode = CalcMode.FIRSTMODE
    var calcModePos: Int? = null
    var varModePos: Int? = null
    var hapticPref: Boolean = true
}