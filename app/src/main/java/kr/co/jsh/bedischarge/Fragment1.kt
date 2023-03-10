package kr.co.jsh.bedischarge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.jsh.bedischarge.databinding.ItemBinding


class Fragment1 : Fragment() {
    val viewNum: Int = 1
    lateinit var binding: ItemBinding
    lateinit var mainactivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainactivity = context as MainActivity
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        binding.btnDelete.setOnClickListener {
            deleteData()
            (activity as MainActivity).onResume()
        }
        binding.btnEdit.setOnClickListener {
            editData()
            (activity as MainActivity).onResume()
        }

    }

    fun deleteData() {
        MyApplication.prefs.remove("SavedUserName${viewNum}")
        MyApplication.prefs.remove("SavedUserEnlistmentDate${viewNum}")
        MyApplication.prefs.remove("SavedUserDischargeDate${viewNum}")
        MyApplication.prefs.remove("SavedUserArmyType${viewNum}")
        MyApplication.prefs.remove("SavedUserPromotionDate1${viewNum}")
        MyApplication.prefs.remove("SavedUserPromotionDate2${viewNum}")
        MyApplication.prefs.remove("SavedUserPromotionDate3${viewNum}")
        MyApplication.prefs.remove("SavedUserTotalServiceDay${viewNum}")
        MyApplication.prefs.remove("SavedUserNowServiceDay${viewNum}")
        MyApplication.prefs.remove("SavedUserLeftServiceDay${viewNum}")
        MyApplication.prefs.remove("SavedUserServiceRate${viewNum}")
        MyApplication.prefs.remove("SavedUserClasses${viewNum}")
        MyApplication.prefs.remove("SavedUserLeftSalaryDay$viewNum")
        MyApplication.prefs.remove("SavedUserLeftPromotionDay$viewNum")
        MyApplication.prefs.remove("SavedUserNextSalaryRate$viewNum")
    }
    fun editData() {
        val intent = Intent(context, DataEditActivity::class.java)
        intent.putExtra("editView", viewNum)
        startActivity(intent)
    }

    fun loadData() {

        val userDataRefresh = UserDataRefresh(today, viewNum)
        userDataRefresh.setUserDataPlus()

        val name = MyApplication.prefs.getString("SavedUserName$viewNum", "noName")
        val enlistmentDate = MyApplication.prefs.getString("SavedUserEnlistmentDate$viewNum", "NONO")
        val dischargeDate = MyApplication.prefs.getString("SavedUserDischargeDate$viewNum", "NONO")
        val armyType = MyApplication.prefs.getString("SavedUserArmyType$viewNum", "NONO")
        val totalServiceDay = MyApplication.prefs.getString("SavedUserTotalServiceDay$viewNum", "-1")
        val nowServiceDay = MyApplication.prefs.getString("SavedUserNowServiceDay$viewNum", "-1")
        val leftServiceDay = MyApplication.prefs.getString("SavedUserLeftServiceDay$viewNum", "-1")
        val period = enlistmentDate.substring(0,10) + " " + "~" + " " + dischargeDate.substring(0,10)
        val serviceRate = MyApplication.prefs.getString("SavedUserServiceRate$viewNum", "0").toDouble()
        val classes = MyApplication.prefs.getString("SavedUserClasses$viewNum", "NoData")
        val nextClassesRate = MyApplication.prefs.getString("SavedUserNextClassesRate$viewNum", "0").toDouble()
        val nextSalaryRate = MyApplication.prefs.getString("SavedUserNextSalaryRate$viewNum", "0").toDouble()
        val leftPromotionDay = MyApplication.prefs.getString("SavedUserLeftPromotionDay$viewNum", "0")
        val leftSalaryDay = MyApplication.prefs.getString("SavedUserLeftSalaryDay$viewNum", "0")

        binding.ddayText.text = leftServiceDay
        binding.nameText.text = name
        binding.totalServiceDayText.text = totalServiceDay
        binding.nowServiceDayText.text = nowServiceDay
        binding.leftServiceDayText.text = leftServiceDay
        binding.periodText.text = period
        binding.totalProgressBar.progress = serviceRate.toInt()
        binding.classessText.text = classes
        binding.armyTypeText.text = "대한민국 $armyType"
        binding.nextClassesProgressBar.progress = nextClassesRate.toInt()
        binding.nextSalaryProgressBar.progress = nextSalaryRate.toInt()
        binding.totalRateText.text = serviceRate.toString()
        binding.nextSalaryRate.text = nextSalaryRate.toString()
        binding.nextClassesRate.text = nextClassesRate.toString()
        binding.leftWeekText.text = (leftServiceDay.toInt() / 7).toString()
        binding.nextClassesText.text = leftPromotionDay
        binding.nextSalaryText.text = leftSalaryDay

        if (leftServiceDay.toInt() <= 0)
        {
            binding.totalRateText.text = "100!!"
            binding.classessText.text = "민간인"
            binding.armyTypeText.text = "축하드립니다!!"
            binding.nowServiceDayText.text = totalServiceDay
            binding.leftServiceDayText.text = "0"
            binding.ddayText.text = "Day"
            binding.leftWeekText.text = "0"
        }

        if (nowServiceDay.toInt() < 0)
        {
            binding.armyTypeText.text = "훈련병(진)"
            binding.nowServiceDayText.text = "0"
            binding.leftServiceDayText.text = totalServiceDay
            binding.nowServiceDayText.text = "0"
            binding.leftWeekText.text = (totalServiceDay.toInt() / 7).toString()
            binding.nextSalaryText.text = "0"
            binding.nextSalaryRate.text = "0"
            binding.nextSalaryProgressBar.progress = 0
        }
    }

}
