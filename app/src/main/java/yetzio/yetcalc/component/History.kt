package yetzio.yetcalc.component

import android.content.Context
import androidx.preference.PreferenceManager
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import yetzio.yetcalc.model.HistoryItem
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class History{
    val mdb_name = "calchistory.json"
    var ctx: Context? = null
    val m_Mapper = jacksonObjectMapper()

    val dateFormats = listOf(
        FormatStyle.MEDIUM, // Example: Nov 5, 2023 3:30:00 PM
        DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss"), // Example: 05, November 2023 15:30:00
        DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss") // Example: 2023, November 05 15:30:00
    )

    fun addToDb(ex: String, res: String){
        val prefMgr = ctx?.let { PreferenceManager.getDefaultSharedPreferences(it) }

        val datefmr = when(prefMgr?.getString("datehistkey", "Default format")){
            "dd, MMMM yyyy HH:mm:ss" -> DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss").withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString()
            "yyyy, MMMM dd HH:mm:ss" -> DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss").withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString()
            else -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString()
        }

        val histout = HistoryItem(datefmr, ex, res)

        val dbfile = File(ctx?.filesDir, mdb_name)
        var histList = arrayListOf<HistoryItem>()

        if(!dbfile.exists()){
            dbfile.createNewFile()
        }
        else{
            val dbcontent = ctx?.openFileInput(mdb_name)?.bufferedReader().use {
                it?.readText().toString()
            }

            try{
                histList = m_Mapper.readValue(dbcontent, object: TypeReference<ArrayList<HistoryItem>>(){})
            }
            catch (e: Exception){
                println("histList read error occurred")
            }
        }

        histList.add(histout)

        emptyDb()

        ctx?.openFileOutput(mdb_name, Context.MODE_PRIVATE or Context.MODE_APPEND).use {
            it?.write(m_Mapper.writeValueAsString(histList).toByteArray())
        }

    }

    fun getHistoryItems(): ArrayList<HistoryItem>{

        var ret_list = arrayListOf<HistoryItem>()
        val dbfile = File(ctx?.filesDir, mdb_name)
        val prefMgr = ctx?.let { PreferenceManager.getDefaultSharedPreferences(it) }

        if(dbfile.exists()){
            val dbcontent = ctx?.openFileInput(mdb_name)?.bufferedReader().use {
                it?.readText().toString()
            }

            try{
                ret_list = m_Mapper.readValue(dbcontent, object: TypeReference<ArrayList<HistoryItem>>(){})
                for(histItem in ret_list){
                    val datefmr = when(detectDateFormat(histItem.timestamp, dateFormats)){
                        "dd, MMMM yyyy HH:mm:ss" -> DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss").withZone(ZoneOffset.systemDefault())
                        "yyyy, MMMM dd HH:mm:ss" -> DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss").withZone(ZoneOffset.systemDefault())
                        else -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneOffset.systemDefault())
                    }

                    val parsedInstant = datefmr.parse(histItem.timestamp, Instant::from)
                    val desFmr = when(prefMgr?.getString("datehistkey", "Default format")){
                        "dd, MMMM yyyy HH:mm:ss" -> DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss").withZone(ZoneOffset.systemDefault())
                        "yyyy, MMMM dd HH:mm:ss" -> DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss").withZone(ZoneOffset.systemDefault())
                        else -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneOffset.systemDefault())
                    }

                    val formattedDate = desFmr.format(parsedInstant)
                    histItem.timestamp = formattedDate
                }
            }
            catch (e: Exception){
                println("history get list read error occurred")
            }
        }

        // sort in descending order
        ret_list.reverse()

        return ret_list
    }

    fun emptyDb(){
        val dbfile = File(ctx?.filesDir, mdb_name)

        if(dbfile.exists()){
            dbfile.delete()
            dbfile.createNewFile()
        }
    }

    private fun detectDateFormat(dateString: String, dateFormats: List<Any>): String? {
        for (format in dateFormats) {
            try {
                if (format is FormatStyle) {
                    Instant.from(DateTimeFormatter.ofLocalizedDateTime(format).parse(dateString))
                    if (format == FormatStyle.MEDIUM) {
                        return "FormatStyle.MEDIUM"
                    }
                } else if (format is DateTimeFormatter) {
                    Instant.from(format.parse(dateString))
                    if (format.toString() == "dd, MMMM yyyy HH:mm:ss") {
                        return "dd, MMMM yyyy HH:mm:ss"
                    } else if (format.toString() == "yyyy, MMMM dd HH:mm:ss") {
                        return "yyyy, MMMM dd HH:mm:ss"
                    }
                }
            } catch (e: Exception) {
                // println("date time format didn't match")
            }
        }
        return null
    }


}
