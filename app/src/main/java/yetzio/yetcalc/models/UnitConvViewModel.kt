package yetzio.yetcalc.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yetzio.yetcalc.enums.UnitType

class UnitConvViewModel: ViewModel(){

    fun setUnitPosition(isFirst: Boolean, unitType: UnitType?, position: Int, value: String) {
        if(isFirst) {
            when (unitType) {
                UnitType.CURRENCY -> _firstcur.value = value
                UnitType.ANGLE -> _angleftpos.value = position
                UnitType.AREA -> _areaftpos.value = position
                UnitType.DATA -> _dataftpos.value = position
                UnitType.ENERGY -> _energyftpos.value = position
                UnitType.LENGTH -> _lengthftpos.value = position
                UnitType.POWER -> _powerftpos.value = position
                UnitType.PRESSURE -> _preftpos.value = position
                UnitType.SPEED -> _speedftpos.value = position
                UnitType.TEMPERATURE -> _tempftpos.value = position
                UnitType.TIME -> _timeftpos.value = position
                UnitType.VOLUME -> _volumeftpos.value = position
                UnitType.WEIGHT -> _wmftpos.value = position
                else -> { /* */ }
            }
        } else {
            when (unitType) {
                UnitType.CURRENCY -> _secondcur.value = value
                UnitType.ANGLE -> _anglesdpos.value = position
                UnitType.AREA -> _areasdpos.value = position
                UnitType.DATA -> _datasdpos.value = position
                UnitType.ENERGY -> _energysdpos.value = position
                UnitType.LENGTH -> _lengthsdpos.value = position
                UnitType.POWER -> _powersdpos.value = position
                UnitType.PRESSURE -> _presdpos.value = position
                UnitType.SPEED -> _speedsdpos.value = position
                UnitType.TEMPERATURE -> _tempsdpos.value = position
                UnitType.TIME -> _timesdpos.value = position
                UnitType.VOLUME -> _volumesdpos.value = position
                UnitType.WEIGHT -> _wmsdpos.value = position
                else -> { /* */ }
            }
        }
    }

    var currentCheckedChip: Int = -1

    var current_date: MutableLiveData<String> = MutableLiveData("")

    var first: MutableLiveData<Boolean> = MutableLiveData(true)
    var currentUnit : MutableLiveData<UnitType> = MutableLiveData(UnitType.CURRENCY)

    var _firstcur: MutableLiveData<String?> = MutableLiveData("INR")
    var _secondcur: MutableLiveData<String?> = MutableLiveData("USD")

    var isChipAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    var _localPinList: MutableLiveData<MutableSet<String>?> = MutableLiveData(mutableSetOf())

    var _lengthftpos: MutableLiveData<Int?> = MutableLiveData()
    var _lengthsdpos: MutableLiveData<Int?> = MutableLiveData()

    var _volumeftpos: MutableLiveData<Int?> = MutableLiveData()
    var _volumesdpos: MutableLiveData<Int?> = MutableLiveData()

    var _areaftpos: MutableLiveData<Int?> = MutableLiveData()
    var _areasdpos: MutableLiveData<Int?> = MutableLiveData()

    var _wmftpos: MutableLiveData<Int?> = MutableLiveData()
    var _wmsdpos: MutableLiveData<Int?> = MutableLiveData()

    var _tempftpos: MutableLiveData<Int?> = MutableLiveData()
    var _tempsdpos: MutableLiveData<Int?> = MutableLiveData()

    var _speedftpos: MutableLiveData<Int?> = MutableLiveData()
    var _speedsdpos: MutableLiveData<Int?> = MutableLiveData()

    var _powerftpos: MutableLiveData<Int?> = MutableLiveData()
    var _powersdpos: MutableLiveData<Int?> = MutableLiveData()

    var _energyftpos: MutableLiveData<Int?> = MutableLiveData()
    var _energysdpos: MutableLiveData<Int?> = MutableLiveData()

    var _preftpos: MutableLiveData<Int?> = MutableLiveData()
    var _presdpos: MutableLiveData<Int?> = MutableLiveData()

    var _timeftpos: MutableLiveData<Int?> = MutableLiveData()
    var _timesdpos: MutableLiveData<Int?> = MutableLiveData()

    var _angleftpos: MutableLiveData<Int?> = MutableLiveData()
    var _anglesdpos: MutableLiveData<Int?> = MutableLiveData()

    var _dataftpos: MutableLiveData<Int?> = MutableLiveData()
    var _datasdpos: MutableLiveData<Int?> = MutableLiveData()
}