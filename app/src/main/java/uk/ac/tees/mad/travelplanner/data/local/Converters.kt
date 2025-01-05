package uk.ac.tees.mad.travelplanner.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromPhotoDataList(photoData: List<ByteArray>?): String? {
        return photoData?.map { it.toBase64() }?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toPhotoDataList(data: String?): List<ByteArray>? {
        return data?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson<List<String>>(it, listType).map { it.fromBase64() }
        }
    }

    private fun ByteArray.toBase64(): String =
        android.util.Base64.encodeToString(this, android.util.Base64.DEFAULT)

    private fun String.fromBase64(): ByteArray =
        android.util.Base64.decode(this, android.util.Base64.DEFAULT)

}
