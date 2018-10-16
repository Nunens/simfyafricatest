package nunens.co.za.simfyafricatest.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nunens.co.za.simfyafricatest.App
import nunens.co.za.simfyafricatest.R

class MainActivity : AppCompatActivity() {
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                104)
    }

    override fun onResume() {
        super.onResume()
        setupPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstTask.setOnClickListener {
            //injecting Vibrator services
            App.vb!!.vibrate(20)

            //starting a new activity
            startActivity(Intent(applicationContext, FirstTaskActivity::class.java))
        }
        secondTask.setOnClickListener {
            //injecting Vibrator services
            App.vb!!.vibrate(20)

            //starting a new activity
            startActivity(Intent(applicationContext, SecondTaskActivity::class.java))
        }
        thirdTask.setOnClickListener {
            //injecting Vibrator services
            App.vb!!.vibrate(20)

            //starting a new activity
            startActivity(Intent(applicationContext, ThirdTaskActivity::class.java))
        }
    }


}
