package kr.co.jsh.bedischarge

import java.lang.Character.getNumericValue
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class UserDataPlus(var classes: String, var totalServiceDay: Int,
                        var nowServiceDay: Int, var leftServiceDay: Int, var serviceRate: Double,
                        var nextClassesRate: Double, var nextSalaryRate: Double)

class UserDataRefresh(val today:String, val viewNum: Int) {

    fun setUserDataPlus() {
        setClasses()
        setTotalServiceDay()
        setNowServiceDay()
        setLeftServiceDay()
        setLeftSalaryDay()
        setLeftPromotionDay()
        setServiceRate()
        setNextClassesRate()
        setNextSalaryRate()
    }

    fun refreshRate() {
        setServiceRate()
        setNextClassesRate()
        setNextSalaryRate()
    }

    fun setClasses() {
        val pDate1 = MyApplication.prefs.getString("SavedUserPromotionDate1$viewNum",
            "0").replace(".", "").toLong()
        val pDate2 = MyApplication.prefs.getString("SavedUserPromotionDate2$viewNum",
            "0").replace(".", "").toLong()
        val pDate3 = MyApplication.prefs.getString("SavedUserPromotionDate3$viewNum",
            "0").replace(".", "").toLong()
        val enlistmentDate = MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum",
            "0").replace(".", "").toLong()
        val dischargeDate = MyApplication.prefs.getString("SavedUserDischargeDate$viewNum",
            "0").replace(".", "").toLong()
        val nowDay = today.replace(".", "").toLong()

        if ((enlistmentDate <= nowDay) && (nowDay < pDate1)) {
            MyApplication.prefs.setString("SavedUserClasses$viewNum", "이병")
        }
        else if ((pDate1 <= nowDay) && (nowDay < pDate2)) {
            MyApplication.prefs.setString("SavedUserClasses$viewNum", "일병")
        }
        else if ((pDate2 <= nowDay) && (nowDay < pDate3)) {
            MyApplication.prefs.setString("SavedUserClasses$viewNum", "상병")
        }
        else if ((pDate3 <= nowDay) && (nowDay < dischargeDate)) {
            MyApplication.prefs.setString("SavedUserClasses$viewNum", "병장")
        }
        else if (nowDay >= dischargeDate) {
            MyApplication.prefs.setString("SavedUserClasses$viewNum", "민간인")
        }
    }

    fun setTotalServiceDay() {
        val startDay = MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum", "0")
        val lastDay = MyApplication.prefs.getString("SavedUserDischargeDate$viewNum", "0")
        val fewDay = calDay(startDay, lastDay) / (24*60*60*1000) + 1

        MyApplication.prefs.setString("SavedUserTotalServiceDay$viewNum", fewDay.toString())
    }

    fun setNowServiceDay() {
        val startDay = MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum", "0")
        val lastDay = today
        val fewDay = calDay(startDay, lastDay) / (24*60*60*1000) + 1

        MyApplication.prefs.setString("SavedUserNowServiceDay$viewNum", fewDay.toString())
    }

    fun setLeftServiceDay() {
        val total = MyApplication.prefs.getString("SavedUserTotalServiceDay$viewNum", "0").toLong()
        val now = MyApplication.prefs.getString("SavedUserNowServiceDay$viewNum", "0").toLong()

        MyApplication.prefs.setString("SavedUserLeftServiceDay$viewNum", (total-now).toString())
    }
    fun setLeftPromotionDay() {
        val nowClasses = MyApplication.prefs.getString("SavedUserClasses$viewNum", "NoData")

        if (nowClasses == "이병") {
            val startDay =
                MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserPromotionDate1$viewNum", "NoData")

            val total = calDay(startDay, lastDay) / (24 * 60 * 60 * 1000)
            val now = calDay(startDay, today) / (24 * 60 * 60 * 1000)
            val leftDay = total - now

            MyApplication.prefs.setString("SavedUserLeftPromotionDay$viewNum", leftDay.toString())
        }

        if (nowClasses == "일병") {
            val startDay =
                MyApplication.prefs.getString("SavedUserPromotionDate1$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserPromotionDate2$viewNum", "NoData")

            val total = calDay(startDay, lastDay) / (24 * 60 * 60 * 1000)
            val now = calDay(startDay, today) / (24 * 60 * 60 * 1000)
            val leftDay = total - now

            MyApplication.prefs.setString("SavedUserLeftPromotionDay$viewNum", leftDay.toString())
        }

        if (nowClasses == "상병") {
            val startDay =
                MyApplication.prefs.getString("SavedUserPromotionDate2$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserPromotionDate3$viewNum", "NoData")

            val total = calDay(startDay, lastDay) / (24 * 60 * 60 * 1000)
            val now = calDay(startDay, today) / (24 * 60 * 60 * 1000)
            val leftDay = total - now

            MyApplication.prefs.setString("SavedUserLeftPromotionDay$viewNum", leftDay.toString())
        }

        if (nowClasses == "병장") {
            val startDay =
                MyApplication.prefs.getString("SavedUserPromotionDate3$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserDischargeDate$viewNum", "NoData")

            val total = calDay(startDay, lastDay) / (24 * 60 * 60 * 1000)
            val now = calDay(startDay, today) / (24 * 60 * 60 * 1000)
            val leftDay = total - now

            MyApplication.prefs.setString("SavedUserLeftPromotionDay$viewNum", leftDay.toString())
        }

        if(MyApplication.prefs.getString("SavedUserLeftServiceDay$viewNum", "1").toInt() <= 0) {
            MyApplication.prefs.setString("SavedUserLeftPromotionDay$viewNum", "0")
        }

    }

    fun setLeftSalaryDay() {
        val std = SimpleDateFormat("yyyy.MM.10.00.00.00")
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val startDay = std.format(calendar.time)
        calendar.add(Calendar.MONTH,1)
        var lastDay = std.format(calendar.time)

        val total = calDay(startDay, lastDay) / (24*60*60*1000)
        val now = calDay(startDay, today) / (24*60*60*1000)

        MyApplication.prefs.setString("SavedUserLeftSalaryDay$viewNum", (total-now).toString())

        if(MyApplication.prefs.getString("SavedUserLeftServiceDay$viewNum", "1").toInt() <= 0) {
            MyApplication.prefs.setString("SavedUserLeftSalaryDay$viewNum", "0")
        }
    }

    fun calDay(startDay: String, lastDay: String): Long {

        var startDaySp = startDay.split(".").toMutableList()
        var lastDaySp = lastDay.split(".").toMutableList()

        if (getNumericValue(startDaySp[1][0]) == 0) {
            startDaySp[1] = startDaySp[1].substring(1,2)
        }
        if (getNumericValue(lastDaySp[1][0]) == 0) {
            lastDaySp[1] = lastDaySp[1].substring(1,2)
        }
        if (getNumericValue(startDaySp[2][0]) == 0) {
            startDaySp[2] = startDaySp[2].substring(1,2)
        }
        if (getNumericValue(lastDaySp[1][0]) == 0) {
            lastDaySp[2] = lastDaySp[2].substring(1,2)
        }


        val totalStart = Calendar.getInstance().apply {
            set(Calendar.YEAR, startDaySp[0].toInt())
            set(Calendar.MONTH, startDaySp[1].toInt())
            set(Calendar.DAY_OF_MONTH, startDaySp[2].toInt())
            set(Calendar.HOUR_OF_DAY, startDaySp[3].toInt())
            set(Calendar.MINUTE, startDaySp[4].toInt())
            set(Calendar.SECOND, startDaySp[5].toInt())
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val totalLast = Calendar.getInstance().apply {
            set(Calendar.YEAR, lastDaySp[0].toInt())
            set(Calendar.MONTH, lastDaySp[1].toInt())
            set(Calendar.DAY_OF_MONTH, lastDaySp[2].toInt())
            set(Calendar.HOUR_OF_DAY, lastDaySp[3].toInt())
            set(Calendar.MINUTE, lastDaySp[4].toInt())
            set(Calendar.SECOND, lastDaySp[5].toInt())
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val fewDay = totalLast - totalStart

        return fewDay

    }

    fun setServiceRate() {
        val enlist = MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum", "0")
        val discharge = MyApplication.prefs.getString("SavedUserDischargeDate$viewNum", "0")
        val rate = calDay(enlist,today).toDouble() / calDay(enlist,discharge).toDouble() * 100

        MyApplication.prefs.setString("SavedUserServiceRate$viewNum", String.format("%.6f", rate))

    }

    fun setNextClassesRate() {
        val nowClasses = MyApplication.prefs.getString("SavedUserClasses$viewNum", "NoData")

        if (nowClasses == "이병") {
            val startDay =
                MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserPromotionDate1$viewNum", "NoData")

            val total = calDay(startDay, lastDay).toDouble()
            val now = calDay(startDay, today).toDouble()
            val rate = now / total * 100

            MyApplication.prefs.setString(
                "SavedUserNextClassesRate$viewNum",
                String.format("%.6f", rate)
            )
        }

        if (nowClasses == "일병") {
            val startDay =
                MyApplication.prefs.getString("SavedUserPromotionDate1$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserPromotionDate2$viewNum", "NoData")

            val total = calDay(startDay, lastDay).toDouble()
            val now = calDay(startDay, today).toDouble()
            val rate = now / total * 100

            MyApplication.prefs.setString(
                "SavedUserNextClassesRate$viewNum",
                String.format("%.6f", rate)
            )
        }

        if (nowClasses == "상병") {
            val startDay =
                MyApplication.prefs.getString("SavedUserPromotionDate2$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserPromotionDate3$viewNum", "NoData")

            val total = calDay(startDay, lastDay).toDouble()
            val now = calDay(startDay, today).toDouble()
            val rate = now / total * 100

            MyApplication.prefs.setString(
                "SavedUserNextClassesRate$viewNum",
                String.format("%.6f", rate)
            )
        }

        if (nowClasses == "병장") {
            val startDay =
                MyApplication.prefs.getString("SavedUserPromotionDate3$viewNum", "NoData")
            val lastDay = MyApplication.prefs.getString("SavedUserDischargeDate$viewNum", "NoData")

            val total = calDay(startDay, lastDay).toDouble()
            val now = calDay(startDay, today).toDouble()
            val rate = now / total * 100

            MyApplication.prefs.setString(
                "SavedUserNextClassesRate$viewNum",
                String.format("%.6f", rate)
            )
        }

        if(MyApplication.prefs.getString("SavedUserLeftServiceDay$viewNum", "1").toInt() <= 0) {
            MyApplication.prefs.setString("SavedUserNextClassesRate$viewNum", "0")
        }

    }

    fun setNextSalaryRate() {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy.01.10.00.00.00")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy.02.10.00.00.00")
        val startDay = LocalDateTime.now().format(formatter1)
        val lastDay = LocalDateTime.now().format(formatter2)
        val formatter3 = DateTimeFormatter.ofPattern("yyyy.01.dd.HH.mm.dd")
        val std = LocalDateTime.now().format(formatter3)

        val total = calDay(startDay, lastDay).toDouble()
        val now = calDay(startDay, std).toDouble()
        val rate = now / total * 100

        MyApplication.prefs.setString("SavedUserNextSalaryRate$viewNum", String.format("%.6f", rate))

        if(MyApplication.prefs.getString("SavedUserLeftServiceDay$viewNum", "1").toInt() <= 0) {
            MyApplication.prefs.setString("SavedUserNextSalaryRate$viewNum", "0")
        }
    }

}