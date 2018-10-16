package nunens.co.za.simfyafricatest.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_secont_task_list.*
import menolla.co.za.itsi_test.adapter.SecondTaskAdapter
import nunens.co.za.simfyafricatest.App.Companion.context
import nunens.co.za.simfyafricatest.R
import nunens.co.za.simfyafricatest.database.model.SecondTaskListModel
import nunens.co.za.simfyafricatest.listener.AdapterClickListener

class SecondTaskListActivity : AppCompatActivity(), AdapterClickListener {
    override fun onClick(model: SecondTaskListModel) {

    }

    var arrayList: ArrayList<SecondTaskListModel> = ArrayList()
    var url = "http://placehold.it/2048&text=Item "
    var model: SecondTaskListModel? = null
    var sharedPref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secont_task_list)
        sharedPref = getSharedPreferences("SimfySession", Context.MODE_PRIVATE)
        welcome.text = "Welcome: " + sharedPref!!.getString("username", "Simfy")
        var linearLayoutManager = LinearLayoutManager(context)
        secont_task_list.layoutManager = linearLayoutManager
        synchronized(this) {
            for (i in 0 until 250) {
                Log.e("SEcond", "I = $i")
                model = SecondTaskListModel("")
                model!!.image = url + (i + 1)
                model!!.title = "Item " + (i + 1)
                model!!.description = "This is the description for Item " + (i + 1)
                Log.e("SEcondMOdel", "I = ${Gson().toJson(model)}")
                arrayList.add(model!!)
            }
        }
        Log.e("Full List", "I = ${Gson().toJson(arrayList)}")
        val adapter = SecondTaskAdapter(arrayList)
        secont_task_list.adapter = adapter
        adapter.setItems(applicationContext, arrayList, this)
    }
    override fun onBackPressed() {
        finish()
    }
}
