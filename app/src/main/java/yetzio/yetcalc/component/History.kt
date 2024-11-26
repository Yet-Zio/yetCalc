package yetzio.yetcalc.component

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import yetzio.yetcalc.models.HistoryItem
import yetzio.yetcalc.models.HistoryViewModel
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalQueries
import java.util.Locale

class History {
    private val mdbName = "calchistory.json"
    var ctx: Context? = null
    var histViewModel: HistoryViewModel? = null
    val type = object : TypeToken<ArrayList<HistoryItem>>() {}.type
    private val gson = Gson()

    val dateFormats = listOf(
        "FormatStyle.MEDIUM" to DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault()),
        "dd, MMMM yyyy HH:mm:ss" to DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss"),
        "yyyy, MMMM dd HH:mm:ss" to DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss")
    )

    fun addToDb(ex: String, res: String) {
        println("DEBUG: Starting write operation for $ex = $res")
        try {
            val prefs = ctx?.getDefSharedPrefs()
            val datefmr = when (prefs?.getString(SharedPrefs.DATEHISTKEY, "Default format")) {
                "dd, MMMM yyyy HH:mm:ss" -> DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss")
                "yyyy, MMMM dd HH:mm:ss" -> DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss")
                else -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            }.withZone(ZoneOffset.systemDefault()).format(Instant.now())

            val histOut = HistoryItem(datefmr, ex, res)
            val dbFile = File(ctx?.filesDir, mdbName)

            // Read existing items
            val histList = if (dbFile.exists()) {
                val dbContent =
                    ctx?.openFileInput(mdbName)?.bufferedReader().use { it?.readText() ?: "" }
                try {
                    gson.fromJson<ArrayList<HistoryItem>>(dbContent, type) ?: arrayListOf()
                } catch (e: Exception) {
                    println("DEBUG: Error reading existing items: ${e.message}")
                    arrayListOf()
                }
            } else {
                println("DEBUG: Creating new database file")
                dbFile.createNewFile()
                arrayListOf()
            }

            histList.add(histOut)

            // Force synchronous write and flush
            ctx?.openFileOutput(mdbName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream?.let {
                    val content = gson.toJson(histList)
                    it.write(content.toByteArray())
                    it.flush()
                    println("DEBUG: Write completed successfully")
                    histViewModel?.listCount?.postValue(histViewModel?.listCount?.value?.plus(1))
                    println("listcount histlist ${histViewModel?.listCount?.value}")
                }
            }
        } catch (e: Exception) {
            println("DEBUG: Error during write: ${e.message}")
            throw e
        } finally {
            println("DEBUG: Write operation completed")
        }
    }

    fun getHistoryItems(): ArrayList<HistoryItem> {
        println("DEBUG: Starting read operation")
        val retList = arrayListOf<HistoryItem>()
        val dbFile = File(ctx?.filesDir, mdbName)

        if (dbFile.exists()) {
            val dbContent =
                ctx?.openFileInput(mdbName)?.bufferedReader().use { it?.readText() ?: "" }
            println("DEBUG: Read content length: ${dbContent.length}")

            if (dbContent.isNotBlank()) {
                try {
                    val histItems = gson.fromJson<ArrayList<HistoryItem>>(dbContent, type)
                    println("DEBUG: Parsed ${histItems.size} items")

                    for (histItem in histItems) {
                        val datefmr = when (detectDateFormat(histItem.timestamp, dateFormats)) {
                            "dd, MMMM yyyy HH:mm:ss" -> DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss")
                            "yyyy, MMMM dd HH:mm:ss" -> DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss")
                            else -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        }.withZone(ZoneOffset.systemDefault())

                        val parsedInstant = datefmr.parse(histItem.timestamp, Instant::from)
                        val desFmr = when (ctx?.getDefSharedPrefs()
                            ?.getString(SharedPrefs.DATEHISTKEY, "Default format")) {
                            "dd, MMMM yyyy HH:mm:ss" -> DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss")
                            "yyyy, MMMM dd HH:mm:ss" -> DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss")
                            else -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        }.withZone(ZoneOffset.systemDefault())

                        val formattedDate = desFmr.format(parsedInstant)
                        histItem.timestamp = formattedDate
                    }
                    retList.addAll(histItems.reversed())
                } catch (e: Exception) {
                    println("DEBUG: Error parsing history: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                println("DEBUG: Database file is empty")
            }
        } else {
            println("DEBUG: Database file does not exist")
        }

        println("DEBUG: Returning ${retList.size} items")
        return retList

    }

    fun emptyDB() {
        println("DEBUG: Emptying database")
        val dbfile = File(ctx?.filesDir, mdbName)
        if (dbfile.exists()) {
            dbfile.delete()
            dbfile.createNewFile()
            println("DEBUG: Database cleared and recreated")
            histViewModel?.listCount?.value = 0
        }
    }

    private fun detectDateFormat(dateString: String, dateFormats: List<Pair<String, DateTimeFormatter>>): String? {
        for ((formatName, formatter) in dateFormats) {
            try {
                val parsedDate = formatter.parse(dateString)
                if (formatter != DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM) || parsedDate.query(
                        TemporalQueries.zoneId()) != null) {
                    return formatName
                }
            } catch (e: Exception) {
                println("DEBUG: Format $formatName didn't match.")
            }
        }
        return null
    }


}
