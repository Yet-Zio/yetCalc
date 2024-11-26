package yetzio.yetcalc.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EasterEggViewModel: ViewModel() {
    var clickCount: MutableLiveData<Int> = MutableLiveData(0)
}