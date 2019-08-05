package Carder_telegram

import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Exception
import java.lang.StringBuilder

data class jsonLookupPattern(
           val numeric:String,
           val country:JsonObject,
            val bank:JsonObject)

class bin(cc:String){
    var bin:String=""
    set(value:String) {
        field = value.subSequence(0,6).toString()
    }
    get() = field
    init{
        bin=cc
    }
    fun lookupGetBin():ArrayList<ArrayList<String>>?{
        println("BIN $bin")
        var page = khttp.get("https://lookup.binlist.net/$bin")
        if(page.statusCode==404) {
            println("404")
            return null
        }else {
            var text = page.text
            val data = listOf(listOf<String>("alpha2","name", "emoji"),
                                        listOf<String>("name", "url", "phone"))
            var countryArr = ArrayList<String>()
            var bankArr = ArrayList<String>()
            println("[$text]")
            var json = Gson().fromJson(text, jsonLookupPattern::class.java)
            println(json)

            for(semaphore in 0..1) {
                for (x in 0..data[semaphore].size - 1) {
                    try {
                        if(semaphore==0) countryArr.add(json.country.get(data[semaphore][x]).toString())
                        else {bankArr.add(json.bank.get(data[semaphore][x]).toString())}
                    } catch (e: Exception) {
                    }
                }
            }
            return arrayListOf(countryArr, bankArr)
        }
    }
}