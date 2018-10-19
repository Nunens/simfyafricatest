package nunens.co.za.simfyafricatest.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detailed_third.*
import menolla.co.za.itsi_test.adapter.SecondTaskAdapter
import menolla.co.za.itsi_test.utils.ToastUtil
import nunens.co.za.simfyafricatest.App
import nunens.co.za.simfyafricatest.R
import nunens.co.za.simfyafricatest.service.MusicService

class DetailedThirdActivity : AppCompatActivity() {
    var myTitle: String = ""
    var myDescription: String = ""
    var myImage: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_third)
        myTitle = intent.extras.getString("title")
        myDescription = intent.extras.getString("description")
        myImage = intent.extras.getString("image")

        t_title.text = myTitle
        t_description.text = myDescription
        try {
            Glide.with(SecondTaskAdapter.ctx)
                    .apply {
                        RequestOptions()
                                .error(R.drawable.placeholder)
                                .centerCrop()
                    }
                    .load(myImage)
                    .into(t_image)
        } catch (e: Exception) {
            ToastUtil.errorToast(SecondTaskAdapter.ctx!!, "Unable to load image")
        }

        play.setOnClickListener {
            try {
                //Start a music player service
                MusicService.startActionPlay(applicationContext, R.raw.meow)
                
                //injecting Vibrator services
                App.vb!!.vibrate(20)
            } catch (e: Exception) {
                ToastUtil.errorToast(applicationContext, "Error playing sound")
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
