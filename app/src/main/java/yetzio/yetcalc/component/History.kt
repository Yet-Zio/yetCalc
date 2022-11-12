package yetzio.yetcalc.component

import android.content.Context
import android.os.Build
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

    fun addToDb(ex: String, res: String){
        val datefmr = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now()).toString()

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

        if(dbfile.exists()){
            val dbcontent = ctx?.openFileInput(mdb_name)?.bufferedReader().use {
                it?.readText().toString()
            }

            try{
                ret_list = m_Mapper.readValue(dbcontent, object: TypeReference<ArrayList<HistoryItem>>(){})
            }
            catch (e: Exception){
                println("history get list read error occurred")
            }
        }

        return ret_list
    }

    fun emptyDb(){
        val dbfile = File(ctx?.filesDir, mdb_name)

        if(dbfile.exists()){
            dbfile.delete()
            dbfile.createNewFile()
        }
    }
}
