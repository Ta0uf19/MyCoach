package com.vivekkaushik.datepicker.helper

import android.util.Log
import org.joda.time.DateTime
import java.util.ArrayList
import java.util.Date
import java.util.HashMap

interface Utils{
    companion object{
        fun getMapperDate(dateTag : String) : String {
            Log.d("utils.kta", dateTag);
            val date = HashMap<String, String>().apply {
                this["SUN"] = "Minggu"
                this["MON"] = "Senin"
                this["TUE"] = "Selasa"
                this["WED"] = "Rabu"
                this["THU"] = "Kamis"
                this["FRI"] = "Jum'at"
                this["SAT"] = "Sabtu"
            }
            return date[dateTag]!!
        }

        fun generateDateRange(
            start: DateTime,
            end: DateTime
        ): List<DateTime> {
            val ret: MutableList<DateTime> =
                ArrayList()
            var tmp = start
            while (tmp.isBefore(end) || tmp == end) {
                ret.add(tmp)
                tmp = tmp.plusDays(1)
            }
            return ret
        }

        fun getDateRange(
            dateStart: String?,
            dateEnd: String?
        ): ArrayList<Date>? {
            val start = DateTime.parse(dateStart)
            val end = DateTime.parse(dateEnd)
            val between = generateDateRange(start, end)
            val dates = ArrayList<Date>()
            for (d in between) {
                dates.add(d.toDate())
            }
            return dates
        }
    }
}