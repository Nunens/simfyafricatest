package nunens.co.za.simfyafricatest.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_third_task.*
import menolla.co.za.itsi_test.adapter.SecondTaskAdapter
import menolla.co.za.itsi_test.network.NetworkCheck
import menolla.co.za.itsi_test.utils.ToastUtil
import nunens.co.za.simfyafricatest.App
import nunens.co.za.simfyafricatest.R
import nunens.co.za.simfyafricatest.database.model.SecondTaskListModel
import nunens.co.za.simfyafricatest.listener.AdapterClickListener
import nunens.co.za.simfyafricatest.listener.VolleyListener
import nunens.co.za.simfyafricatest.network.VolleyUtil
import org.jetbrains.anko.progressDialog
import org.json.JSONArray
import org.json.JSONObject

class ThirdTaskActivity : AppCompatActivity(), VolleyListener, AdapterClickListener {
    override fun onClick(model: SecondTaskListModel) {
        startActivity(Intent(applicationContext, DetailedThirdActivity::class.java).putExtra("title", model.title).putExtra("description", model.description).putExtra("image", model.image))
    }

    var model: SecondTaskListModel? = null

    var arrayList: ArrayList<SecondTaskListModel> = ArrayList()
    lateinit var dialog: ProgressDialog
    override fun onResponse(resp: String?) {
        val response = JSONArray(resp)
        synchronized(this) {
            for (i in 0 until response.length()) {
                var singleObj: JSONObject = response.getJSONObject(i)
                model = SecondTaskListModel("")
                model!!.id = singleObj.getString("id")
                model!!.image = singleObj.getString("url")
                model!!.title = "Image " + singleObj.getString("id")
                model!!.description = "This is the description for Item " + (i + 1)
                App!!.db!!.thirdTaskDAO().insert(model!!)
                arrayList!!.add(model!!)
            }
        }
        val adapter = SecondTaskAdapter(ArrayList())
        third_task_list!!.adapter = adapter
        adapter.setItems(applicationContext, arrayList, this)
        dialog.dismiss()
    }

    override fun onError(volleyError: VolleyError?) {
        dialog.dismiss()
        ToastUtil.errorToast(applicationContext, "Unknown error occurred")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_task)
        dialog = progressDialog(message = "Please wait a bit…", title = "Fetching data")
        dialog.isIndeterminate = true
        dialog.setCancelable(false)
        init()
    }

    fun init() {
        var linearLayoutManager = LinearLayoutManager(App.context)
        third_task_list.layoutManager = linearLayoutManager
        if (NetworkCheck.checkWifi(applicationContext) || NetworkCheck.checkMobileNetwork(applicationContext)) {
            dialog.show()
            VolleyUtil.thirdRequest(applicationContext, this)
        } else {
            dialog.dismiss()
            ToastUtil.noNetworkToast(applicationContext)
        }
    }
    override fun onBackPressed() {
        finish()
    }
}
