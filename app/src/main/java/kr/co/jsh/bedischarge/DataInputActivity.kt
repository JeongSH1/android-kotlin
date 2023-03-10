package kr.co.jsh.bedischarge

import android.R
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kr.co.jsh.bedischarge.databinding.ActivityDataInputBinding
import java.text.DecimalFormat
import java.util.*

class DataInputActivity : AppCompatActivity() {

    val binding by lazy { ActivityDataInputBinding.inflate(layoutInflater) }
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val armyTypeList = listOf("-군별-", "육군", "해군", "공군", "해병")
        val armyTypeAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, armyTypeList)
        binding.editArmyType.adapter = armyTypeAdapter

        binding.editUserName.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KEYCODE_ENTER
            ) {
                // 키패드 내리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.editUserName.windowToken, 0)
            }
            false
        }


        binding.editEnlistmentDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val df = DecimalFormat("00")

            DatePickerDialog(   this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                binding.editEnlistmentDate.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        binding.editDischargeDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val df = DecimalFormat("00")

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                binding.editDischargeDate.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }


        val listSize = intent?.getIntExtra("listSize", -1)

        binding.btnSubmit.setOnClickListener {


            if (checkData()) {
                val userData = getData()

                if (listSize != null) {
                    when (listSize) {
                        1 -> {
                            list.add(0, Fragment1())
                            saveData(listSize, userData)
                        }
                        2 -> {
                            list.add(1, Fragment2())
                            saveData(listSize, userData)
                        }
                        3 -> {
                            list.add(2, Fragment3())
                            saveData(listSize, userData)
                        }
                        4 -> {
                            list.add(3, Fragment4())
                            saveData(listSize, userData)
                        }
                        5 -> {
                            list.add(4, Fragment5())
                            saveData(listSize, userData)
                        }
                        else -> {

                        }

                    }
                }
                finish()
            }
        }
    }


    fun saveData(viewNum: Int, userData: UserData) {
        MyApplication.prefs.setString("SavedUserName${viewNum}", userData.name)
        MyApplication.prefs.setString("SavedUserEnlistmentDate${viewNum}", userData.EnlistmentDate + ".00.00.00")
        MyApplication.prefs.setString("SavedUserDischargeDate${viewNum}", userData.dischargeDate + ".00.00.00")
        MyApplication.prefs.setString("SavedUserArmyType${viewNum}", userData.armyType)
        MyApplication.prefs.setString("SavedUserPromotionDate1${viewNum}", userData.pDate1 + ".00.00.00")
        MyApplication.prefs.setString("SavedUserPromotionDate2${viewNum}", userData.pDate2 + ".00.00.00")
        MyApplication.prefs.setString("SavedUserPromotionDate3${viewNum}", userData.pDate3 + ".00.00.00")
    }

    fun getData(): UserData {
        val name = binding.editUserName.text.toString()
        val enlistmentDate = binding.editEnlistmentDate.text.toString()
        val armyType = binding.editArmyType.selectedItem.toString()
        val dischargeDate = binding.editDischargeDate.text.toString()
        val pDate = setAllDate(enlistmentDate, armyType)

        val userData = UserData(name, enlistmentDate, dischargeDate, armyType, pDate[0], pDate[1], pDate[2])

        return userData
    }

    fun checkData(): Boolean {
        if (binding.editEnlistmentDate.text.toString() == "입대일") {
            Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.editDischargeDate.text.toString() == "전역일") {
            Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_LONG).show()
            return false
        }

        if (binding.editArmyType.selectedItem.toString() == "-군별-") {
            Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun setAllDate(enlistmentDate: String, armyType: String): List<String> {
        val sp = enlistmentDate.split(".")
        val df = DecimalFormat("00")
        val year = sp[0].toInt()
        val month = sp[1].toInt()
        lateinit var pDate1: String
        lateinit var pDate2: String
        lateinit var pDate3: String

        when (armyType) {
            "공군" -> {
                if (month + 2 > 12) {
                    pDate1 = "${year + 1}.${df.format((month+2)%12)}.${df.format(18)}"
                } else {
                    pDate1 = "${year}.${df.format(month + 3)}.${df.format(18)}"
                }

                if (month + 8 > 12) {
                    pDate2 = "${year + 1}.${df.format((month+8)%12)}.${df.format(18)}"
                } else {
                    pDate2 = "${year}.${df.format(month + 8)}.${df.format(18)}"
                }

                if (month + 14 > 12) {
                    pDate3 = "${year + 1}.${df.format((month+14)%12)}.${df.format(18)}"
                } else {
                    pDate3 = "${year}.${df.format(month + 14)}.${df.format(18)}"
                }

            }

            else -> {
                if (month + 3 > 12) {
                    pDate1 = "${year + 1}.${df.format((month+3)%12)}.${df.format(1)}"
                } else {
                    pDate1 = "${year}.${df.format(month + 3)}.${df.format(1)}"
                }

                if (month + 9 > 12) {
                    pDate2 = "${year + 1}.${df.format((month+9)%12)}.${df.format(1)}"
                } else {
                    pDate2 = "${year}.${df.format(month + 9)}.${df.format(1)}"
                }

                if (month + 15 > 12) {
                    pDate3 = "${year + 1}.${df.format((month+15)%12)}.${df.format(1)}"
                } else {
                    pDate3 = "${year}.${df.format(month + 9)}.${df.format(1)}"
                }
            }
        }
        return listOf(pDate1, pDate2, pDate3)
    }
}


