package ai.travel.app.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ArrayListConverter {

    @TypeConverter
    fun fromStringArrayList(value: ArrayList<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<String> {
        return try {
            Gson().fromJson<ArrayList<String>>(value) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson<T>(json, object : TypeToken<T>() {}.type)