package com.uoons.india.utils

import android.annotation.SuppressLint
import android.util.Log
import com.uoons.india.utils.AppConstants.EMPTY
import com.uoons.india.utils.CommonUtils.isStringNullOrBlank
import org.lsposed.lsparanoid.Obfuscate
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Obfuscate
object DateTimeUtils {

    const val FORMAT_1 =  "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_2 =  "yyyy-MM-dd HH:mm"
    const val FORMAT_3 = "dd MMM yyyy , hh:mm aaa"
    const val FORMAT_4 = "dd MMM yyyy"
    const val FORMAT_5 = "yyyy-MM-dd"
    const val FORMAT_6 = "hh:mm a"
    const val FORMAT_8 = "yyyy-MM-dd hh:mm a"
    const val FORMAT_9 = "HH:mm"
    const val FORMAT_10 = "EEEE, MMMM, dd,  yyyy hh:mm a"
    const val FORMAT_11 = "HH:mm:ss"
    const val FORMAT_12="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FORMAT_13 = "MMM dd, yyyy"
    const val FORMAT_14 = "MMMM dd, yyyy"

    //EEEE
    //Tuesday, October, 5, 2021 1:10 PM

    fun getCurrentTimeString(timeFormat: String): String {
        var strDate = ""
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat(timeFormat, Locale.ENGLISH)
        val cal = Calendar.getInstance()
        val date = cal.time
        strDate = df.format(date)
        return strDate
    }


    fun getEndDate(slots: String, duration: String): String {

        @SuppressLint("SimpleDateFormat")
        val fmtampm = SimpleDateFormat(FORMAT_6, Locale.ENGLISH)
        val date: Date = fmtampm.parse(slots)
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MINUTE, duration.toInt())
        return fmtampm.format(cal.time)


    }

    fun getStartDate(dateInput: String, dateOutput: String, dateString: String): String {
        var result = ""

        if (!isStringNullOrBlank(dateString)) {
            @SuppressLint("SimpleDateFormat") val formatComingFromServer =
                SimpleDateFormat(dateInput, Locale.ENGLISH)
            @SuppressLint("SimpleDateFormat") val formatRequired =
                SimpleDateFormat(dateOutput, Locale.ENGLISH)


            try {
                val dateN = formatComingFromServer.parse(dateString)
                result = formatRequired.format(dateN)
                Logger.e(AppConstants.LOG_CAT, "! RETURNING PARSED DATE : $result")
            } catch (e: Exception) {
                Logger.e(
                    AppConstants.LOG_CAT,
                    "Some Exception while parsing the date : $e"
                )
            }
        }
        return result
    }

    fun beforeMinutes(dateFormat: String, appointmentDate: String, interval: Int): Date {
        lateinit var appointment: Date
        try {
            val dateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val appointmentD: Date = dateFormat.parse(appointmentDate)
            val addCalender = Calendar.getInstance()
            addCalender.time = appointmentD
            addCalender.set(Calendar.MINUTE, addCalender.get(Calendar.MINUTE) - interval)
            appointment = addCalender.time
            return appointment
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_CAT, "Exception in case of Setting Date Parse $e")
        }
        return appointment
    }

    fun afterHour(dateFormat: String, appointmentDate: String, interval: Int): Date {
        lateinit var appointment: Date
        try {
            val dateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val appointmentD: Date = dateFormat.parse(appointmentDate)
            val addCalender = Calendar.getInstance()
            addCalender.time = appointmentD
            addCalender.set(Calendar.HOUR, addCalender.get(Calendar.HOUR) + interval)
            appointment = addCalender.time
            return appointment
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_CAT, "Exception in case of Setting Date Parse $e")
        }
        return appointment
    }

    fun getDateWithTime(dateInput: String, dateOutput: String, dateString: String): String {
        var result = ""



        if (!CommonUtils.isStringNullOrBlank(dateString)) {
            @SuppressLint("SimpleDateFormat") val formatComingFromServer = SimpleDateFormat(dateInput, Locale.ENGLISH)
            @SuppressLint("SimpleDateFormat") val formatRequired = SimpleDateFormat(dateOutput, Locale.ENGLISH)
            formatComingFromServer.timeZone = TimeZone.getTimeZone("UTC")
            formatRequired.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)

            try {
                val dateN = formatComingFromServer.parse(dateString)
                result = formatRequired.format(dateN)
                Log.e(AppConstants.LOG_CAT, "! RETURNING PARSED DATE : $result")
            } catch (e: Exception) {
                Log.e(AppConstants.LOG_CAT, "Some Exception while parsing the date : $e")
            }
        }
        return result
    }


    fun beforeSlotTime(myTime: String, interval: Int): String {
        Logger.e(AppConstants.LOG_CAT, "interval  $interval")
        val df = SimpleDateFormat(FORMAT_6, Locale.ENGLISH)
        val d = df.parse(myTime)
        val cal = Calendar.getInstance()
        cal.time = d
        cal.add(Calendar.MINUTE, +interval)
        return df.format(cal.time)
    }


    fun convertDateformatDate(date: String?, initDateFormat: String?, endDateFormat: String?): String {
        var formattedDate=""
        try {
            val initDate = SimpleDateFormat(initDateFormat).parse(date)
            val formatter = SimpleDateFormat(endDateFormat)
            formattedDate=formatter.format(initDate)
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
        return  formattedDate
    }


    fun getDateFromString(dateString: String, timeFormat: String): Date {
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat(timeFormat, Locale.ENGLISH)
        return df.parse(dateString)
    }


    fun getDateToString(date: Date, dateformat: String): String {
        val dateFormat = SimpleDateFormat(dateformat, Locale.ENGLISH)
        return dateFormat.format(date)
    }

    fun getDates(startDate: String, endDate: String): List<Date> {
        val dates = ArrayList<Date>()
        val df1: DateFormat = SimpleDateFormat(FORMAT_5)
        var dateStart: Date? = null
        var dateEnd: Date? = null
        try {
            dateStart = df1.parse(startDate)
            dateEnd = df1.parse(endDate)
        } catch (e: ParseException) {
            Logger.e(
                AppConstants.LOG_CAT,
                "Exception in case of application are get dates  $e"
            )
        }
        val calStart = Calendar.getInstance()
        calStart.time = dateStart
        val calEnd = Calendar.getInstance()
        calEnd.time = dateEnd
        while (!calStart.after(calEnd)) {
            dates.add(calStart.time)
            calStart.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun getDateInFormat(dateInput: String, dateOutput: String, _date: String): String {
        return try {
            @SuppressLint("SimpleDateFormat") val inputFormat =  SimpleDateFormat(
                dateInput,
                Locale.ENGLISH
            )
            @SuppressLint("SimpleDateFormat") val outputFormat =  SimpleDateFormat(
                dateOutput,
                Locale.ENGLISH
            )
            val date = inputFormat.parse(_date)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            Logger.e(AppConstants.LOG_CAT, "Exception in date parsing  $e")
            EMPTY
        }
    }



    fun getLastSlot(slots: String, duration: Int): String {

        @SuppressLint("SimpleDateFormat")
        val fmtampm = SimpleDateFormat(FORMAT_6, Locale.ENGLISH)
        val date: Date = fmtampm.parse(slots)
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MINUTE, duration)
        return fmtampm.format(cal.time)


    }


    fun parsingDate(dateInput: String, dateOutput: String, dateString: String): Date {
        var result = ""
        lateinit var resultDate: Date
        if (!isStringNullOrBlank(dateString)) {
            @SuppressLint("SimpleDateFormat") val formatComingFromServer = SimpleDateFormat(
                dateInput,
                Locale.ENGLISH
            )
            @SuppressLint("SimpleDateFormat") val formatRequired = SimpleDateFormat(
                dateOutput,
                Locale.ENGLISH
            )
            try {
                var dateN: Date? = null
                //   Logger.e(Constant.LOG_CAT, "COMING DATE : $dateString")
                dateN = formatComingFromServer.parse(dateString)
                result = formatRequired.format(dateN)
                resultDate = formatRequired.parse(result)

            } catch (e: Exception) {
                Logger.e(
                    AppConstants.LOG_CAT,
                    "Some Exception while parsing the date : $e"
                )
            }
        }
        return resultDate
    }


    fun getCurrentDate(timeFormat: String): String {
        val strDate: String
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat(timeFormat, Locale.ENGLISH)
        val cal = Calendar.getInstance()
        strDate = df.format(cal.time)
        return strDate
    }

    fun splitDateTime(dateTime: String): String {
        var array=dateTime.split(" ")
        return array[1]

    }


    fun splitDateTime1(dateTime: String): String {
        var array=dateTime.split(" ")
        return array[0]

    }

    fun getServerUtcTimeInLocalString(UTCStringObjFromServer: String?): String? {
        try {
            val dateFormatUTCRev = SimpleDateFormat(FORMAT_1)
            dateFormatUTCRev.timeZone = TimeZone.getTimeZone("UTC")
            var UTCFromServer: Date? = null
            UTCFromServer = dateFormatUTCRev.parse(UTCStringObjFromServer)
            val dateFormatterLocal = SimpleDateFormat(FORMAT_1)
            dateFormatterLocal.timeZone = TimeZone.getDefault()
            return dateFormatterLocal.format(UTCFromServer)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }
        return null
    }

    fun getDateZone(timeFormate: String, timeZone: String): String {
        var strDate: String = ""
        val df = SimpleDateFormat(timeFormate, Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone(timeZone)
        strDate = df.format(Calendar.getInstance().time)
        return strDate
    }

    fun parsingDate(
        dateInput: String,
        dateOutput: String,
        dateString: String,
        timeZone: String
    ): Date {
        var result = ""
        lateinit var resultDate: Date
        if (!isStringNullOrBlank(dateString)) {
            @SuppressLint("SimpleDateFormat") val formatComingFromServer =
                SimpleDateFormat(dateInput, Locale.ENGLISH)
            @SuppressLint("SimpleDateFormat") val formatRequired =
                SimpleDateFormat(dateOutput, Locale.ENGLISH)
            /*  formatComingFromServer.timeZone = TimeZone.getTimeZone(timeZone)
               formatRequired.timeZone = TimeZone.getTimeZone(timeZone)*/
            try {
                var dateN: Date? = null
                //   Logger.e(Constant.LOG_CAT, "COMING DATE : $dateString")
                dateN = formatComingFromServer.parse(dateString)
                result = formatRequired.format(dateN)
                resultDate = formatRequired.parse(result)

            } catch (e: Exception) {
                Logger.e(
                    AppConstants.LOG_CAT,
                    "Some Exception while parsing the date : $e"
                )
            }
        }
        return resultDate
    }

    fun parseDate(dateFormat:String,dateZone: String): Date {
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        return simpleDateFormat.parse(dateZone)
    }

    fun currentDate(strFormat:String): Date {
        val dateFmt = SimpleDateFormat(strFormat, Locale.ENGLISH)
        return dateFmt.parse(getDateZone(strFormat, TimeZone.getDefault().id))
    }

    fun currentTime(strFormat:String): Date {
        val fmtampm = SimpleDateFormat(strFormat, Locale.ENGLISH)
        return fmtampm.parse(getDateZone(strFormat, TimeZone.getDefault().id))
    }

    fun checkIsTodayDate(strRequestedDate:String,strFormat:String) : Boolean{
        val dateFmt = SimpleDateFormat(strFormat, Locale.ENGLISH)
        val selectedDate: Date = dateFmt.parse(strRequestedDate)
        if (currentDate(strFormat).compareTo(selectedDate) == 0) {
            return true
        }
        return false

    }

    fun isGreaterTime(strTime :String,strFormat:String) : Boolean
    {
        val fmtampm = SimpleDateFormat(strFormat, Locale.ENGLISH)
        var time = fmtampm.parse(strTime)
        if (time >= currentTime(strFormat)) {
            return true
        }
        return false
    }


    fun compareTime(startTime:String, endTime:String,strFormat:String)  : Boolean {
        val fmtampm = SimpleDateFormat(strFormat, Locale.ENGLISH)
        var timeStart = fmtampm.parse(startTime)
        var timeEnd = fmtampm.parse(endTime)
        if (timeEnd >= timeStart) {
            return true
        }
        return false
    }




}