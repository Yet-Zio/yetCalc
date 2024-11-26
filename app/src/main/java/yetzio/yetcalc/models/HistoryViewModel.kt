package yetzio.yetcalc.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel: ViewModel(){
    var histArrList: MutableLiveData<ArrayList<HistoryItem>> = MutableLiveData(ArrayList())
    var listCount: MutableLiveData<Int> = MutableLiveData<Int>(0)

    companion object {
        private var instance: HistoryViewModel? = null

        fun getInstance(): HistoryViewModel {
            if (instance == null) {
                instance = HistoryViewModel()
            }
            return instance!!
        }
    }
}