package kr.co.jsh.bedischarge

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kr.co.jsh.bedischarge.databinding.ActivityDataEditBinding
import java.text.DecimalFormat
import java.util.*

class DataEditActivity : AppCompatActivity() {

    val binding by lazy { ActivityDataEditBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val editView = intent?.getIntExtra("editView", -1)
        val armyTypeList = listOf("-군별-", "육군", "해군", "공군", "해병")
        val armyTypeAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, armyTypeList)
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

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                binding.editEnlistmentDate.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        binding.editDischargeDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val df = DecimalFormat("00")

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                binding.editEnlistmentDate.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }
        binding.textView5.text =
            MyApplication.prefs.
        getString("SavedUserPromotionDate1$editView","NoData").substring(0,10) + "-" + MyApplication.prefs.
        getString("SavedUserPromotionDate2$editView","NoData").substring(0,10) + "-" + MyApplication.prefs.
        getString("SavedUserPromotionDate3$editView","NoData").substring(0,10)

        binding.editPromotionDate.setOnClickListener {
            val dialog = PromotionDialog(this, editView)
            dialog.showDialog()
            dialog.setOnClickListener(object : PromotionDialog.OnDialogClickListener {
                override fun onClicked(name: String) {
                    binding.textView5.text = name
                }
            }
            )

        }

        val enlist = MyApplication.prefs.getString("SavedUserEnlistmentDate${editView}", "noData").substring(0,10)
        val discharge = MyApplication.prefs.getString("SavedUserDischargeDate${editView}", "noData").substring(0,10)
        binding.editUserName.setText(MyApplication.prefs.getString("SavedUserName${editView}", "noData"))
        binding.editEnlistmentDate.setText(enlist)
        binding.editDischargeDate.setText(discharge)

        binding.btnSubmit.setOnClickListener {

            if (checkData()) {
                val userData = getData()

                if (editView != null) {
                    when (editView) {
                        1 -> {
                            saveData(editView, userData)
                        }
                        2 -> {
                            saveData(editView, userData)
                        }
                        3 -> {
                            saveData(editView, userData)
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
        var pDate = binding.textView5.text.toString().split("-")

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



}

class PromotionDialog(val context: Context, val editView: Int?) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.promotiondialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val p1 = dialog.findViewById<EditText>(R.id.pr1)
        val p2 = dialog.findViewById<EditText>(R.id.pr2)
        val p3 = dialog.findViewById<EditText>(R.id.pr3)
        val cancel = dialog.findViewById<Button>(R.id.btnCancel)
        val finish = dialog.findViewById<Button>(R.id.btnFinish)

        p1.setText(MyApplication.prefs.getString("SavedUserPromotionDate1$editView", "NoData").substring(0,10))
        p2.setText(MyApplication.prefs.getString("SavedUserPromotionDate2$editView", "NoData").substring(0,10))
        p3.setText(MyApplication.prefs.getString("SavedUserPromotionDate3$editView", "NoData").substring(0,10))

        p1.setOnClickListener {
            val cal = Calendar.getInstance()
            val df = DecimalFormat("00")

            DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                p1.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        p2.setOnClickListener {
            val cal = Calendar.getInstance()
            val df = DecimalFormat("00")

            DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                p2.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        p3.setOnClickListener {
            val cal = Calendar.getInstance()
            val df = DecimalFormat("00")

            DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                val date = "$y.${df.format(m+1)}.${df.format(d)}"
                p3.setText(date)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }


        cancel.setOnClickListener {
            dialog.dismiss()
        }
        finish.setOnClickListener {
            onClickListener.onClicked(
                p1.text.toString().substring(0,10) + "-" +
                        p2.text.toString().substring(0,10) + "-" +
                          p3.text.toString().substring(0,10)
            )

            dialog.dismiss()
        }

    }
    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}