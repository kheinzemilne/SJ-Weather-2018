package ca.phoenixmedia.sjweather2018.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

// Json handling class in basically every project where I'm parsing JSON data. Relies on Moshi for
// wrapping and unwrapping to and from objects and lists.
class DataUnwrapper {

    companion object Instance {
        @JvmStatic
        fun fromJsonList(moshi: Moshi, cls: Any, json: String): List<Any> {
            val type = Types.newParameterizedType(List::class.java, cls as Type)
            val adapter = moshi.adapter<List<Any>>(type)
            var dataList = arrayListOf<Any>()
            try {
                dataList = adapter.fromJson(json) as ArrayList<Any>
            } catch (e: Exception) {
                if (json.isNotEmpty()) e.printStackTrace()
            }
            return dataList
        }

        @JvmStatic
        fun fromJson(moshi: Moshi, cls: Any, json: String): Any? {
            val type = Types.newParameterizedType(cls as Type)
            val adapter = moshi.adapter<Any>(type)
            var dataList: Any? = null
            try {
                dataList = adapter.fromJson(json)!!
            } catch (e: Exception) {
                if (json.isNotEmpty()) e.printStackTrace()
            }
            return dataList
        }

        @JvmStatic
        fun toJsonList(moshi: Moshi, cls: Any, data: List<Any>): String {
            val type = Types.newParameterizedType(List::class.java, cls as Type)
            val adapter = moshi.adapter<Any>(type)
            var result = "{}"
            try {
                result = adapter.toJson(data)!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        @JvmStatic
        fun toJson(moshi: Moshi, cls: Any, data: Any): String {
            val type = Types.newParameterizedType(cls as Type)
            val adapter = moshi.adapter<Any>(type)
            var result = "{}"
            try {
                result = adapter.toJson(data)!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}