package yetzio.yetcalc.component

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import yetzio.yetcalc.enums.UnitType
import java.io.File

class UnitConvPin(storeType: UnitType) {
    val store_names = mapOf(
        UnitType.CURRENCY.name to "currencyPins.json", UnitType.ANGLE.name to "anglePins.json", UnitType.AREA.name to "areaPins.json",
        UnitType.DATA.name to "dataPins.json", UnitType.ENERGY.name to "energyPins.json", UnitType.LENGTH.name to "lengthPins.json",
        UnitType.POWER.name to "powerPins.json", UnitType.PRESSURE.name to "pressurePins.json", UnitType.SPEED.name to "speedPins.json",
        UnitType.TEMPERATURE.name to "tempPins.json", UnitType.TIME.name to "timePins.json", UnitType.VOLUME.name to "volPins.json",
        UnitType.WEIGHT.name to "weightPins.json")
    var ctx: Context? = null
    var store_name = "currencyPins.json"
    val type = object: TypeToken<MutableMap<String, String>>() {}.type
    val gson = Gson()

    init {
        store_name = store_names[storeType.name].toString()
    }

    companion object{
        fun getConvPinStore(storeType: UnitType): UnitConvPin{
            return when(storeType){
                UnitType.CURRENCY -> UnitConvPin(UnitType.CURRENCY)
                UnitType.ANGLE -> UnitConvPin(UnitType.ANGLE)
                UnitType.AREA -> UnitConvPin(UnitType.AREA)
                UnitType.DATA -> UnitConvPin(UnitType.DATA)
                UnitType.ENERGY -> UnitConvPin(UnitType.ENERGY)
                UnitType.LENGTH -> UnitConvPin(UnitType.LENGTH)
                UnitType.POWER -> UnitConvPin(UnitType.POWER)
                UnitType.PRESSURE -> UnitConvPin(UnitType.PRESSURE)
                UnitType.SPEED -> UnitConvPin(UnitType.SPEED)
                UnitType.TEMPERATURE -> UnitConvPin(UnitType.TEMPERATURE)
                UnitType.TIME -> UnitConvPin(UnitType.TIME)
                UnitType.VOLUME -> UnitConvPin(UnitType.VOLUME)
                UnitType.WEIGHT -> UnitConvPin(UnitType.WEIGHT)
            }
        }
    }

    fun addToPinStore(newpin: String){
        val storeFile = File(ctx?.filesDir, store_name)
        val resultMap = mutableMapOf<String, String>()

        if (!storeFile.exists()) {
            storeFile.createNewFile()
        } else {
            val storeContent = ctx?.openFileInput(store_name)?.bufferedReader().use {
                it?.readText() ?: ""
            }

            try {
                if (storeContent.isNotEmpty()) {
                    resultMap.putAll(gson.fromJson<MutableMap<String, String>>(storeContent, type) ?: mutableMapOf())
                }
            } catch (e: Exception) {
                println("Error adding unit conv pins: ${e.message}")
            }
        }

        resultMap.put(newpin, newpin)

        ctx?.openFileOutput(store_name, Context.MODE_PRIVATE).use {
            it?.write(gson.toJson(resultMap).toByteArray())
        }
    }

    fun removeFromPinStore(rempin: String){
        val storeFile = File(ctx?.filesDir, store_name)
        val resultMap = mutableMapOf<String, String>()

        if (!storeFile.exists()) {
            storeFile.createNewFile()
        } else {
            val storeContent = ctx?.openFileInput(store_name)?.bufferedReader().use {
                it?.readText() ?: ""
            }

            try {
                if (storeContent.isNotEmpty()) {
                    resultMap.putAll(gson.fromJson<MutableMap<String, String>>(storeContent, type) ?: mutableMapOf())
                }
            } catch (e: Exception) {
                println("Error removing unit conv pins: ${e.message}")
            }
        }

        resultMap.remove(rempin)

        ctx?.openFileOutput(store_name, Context.MODE_PRIVATE).use {
            it?.write(gson.toJson(resultMap).toByteArray())
        }
    }

    fun getAllPins(): MutableMap<String, String>{
        val storeFile = File(ctx?.filesDir, store_name)
        val resultMap = mutableMapOf<String, String>()

        if (!storeFile.exists()) {
            storeFile.createNewFile()
        } else {
            val storeContent = ctx?.openFileInput(store_name)?.bufferedReader().use {
                it?.readText() ?: ""
            }

            try {
                if (storeContent.isNotEmpty()) {
                    resultMap.putAll(gson.fromJson<MutableMap<String, String>>(storeContent, type) ?: mutableMapOf())
                }
            } catch (e: Exception) {
                println("Error reading unit conv pins: ${e.message}")
            }
        }

        return resultMap
    }

}