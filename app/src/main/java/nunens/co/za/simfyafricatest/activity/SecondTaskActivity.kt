package nunens.co.za.simfyafricatest.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second_task.*
import menolla.co.za.itsi_test.network.NetworkCheck
import menolla.co.za.itsi_test.utils.ToastUtil
import nunens.co.za.simfyafricatest.R

class SecondTaskActivity : AppCompatActivity() {
    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_task)
        init()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun init() {
        sharedPref = getSharedPreferences("SimfySession", Context.MODE_PRIVATE)
        editor = sharedPref!!.edit()
        if (sharedPref!!.getString("username", "") != "") {
            startActivity(Intent(applicationContext, SecondTaskListActivity::class.java))
            finish()
        }
        btn_login.setOnClickListener {
            //check if username and password are not empty before making an api call
            when {
                edt_username.text.isEmpty() -> ToastUtil.errorToast(applicationContext, "Please enter username")
                edt_password.text.isEmpty() -> ToastUtil.errorToast(applicationContext, "Please enter password")
                edt_password.text.length < 6 -> ToastUtil.errorToast(applicationContext, "Please enter a minimum of 6 characters")
                else -> {
                    //check if there is network or wifi connectivity before making an api call
                    if (NetworkCheck.checkWifi(applicationContext) || NetworkCheck.checkMobileNetwork(applicationContext)) {
                        editor!!.putString("username", edt_username.text.toString())
                        editor!!.commit()
                        startActivity(Intent(applicationContext, SecondTaskListActivity::class.java))
                    } else {
                        ToastUtil.noNetworkToast(applicationContext)
                    }
                }
            }
        }
    }
}
