package com.devopworld.weatherapp.Utilimport java.text.SimpleDateFormatimport java.util.*class DateTimeUtility {    companion object {        fun getCurrentDateStartTimeStamp(): Long {            val cal: Calendar = Calendar.getInstance()            cal.set(Calendar.HOUR_OF_DAY, 0)            cal.set(Calendar.MINUTE, 0)            cal.set(Calendar.SECOND, 0)            cal.set(Calendar.MILLISECOND, 0)            return cal.timeInMillis/1000        }        fun getCurrentDateEndTimeStamp(): Long {            val cal: Calendar = Calendar.getInstance()            cal.set(Calendar.HOUR_OF_DAY, 23)            cal.set(Calendar.MINUTE, 59)            cal.set(Calendar.SECOND, 59)            cal.set(Calendar.MILLISECOND, 0)            return cal.timeInMillis/1000        }        fun convertTimeStampToDay(timeStamp:Long):String{            val dateTime = SimpleDateFormat("EEEE")            val date = Date(timeStamp*1000)            return dateTime.format(date)        }    }}