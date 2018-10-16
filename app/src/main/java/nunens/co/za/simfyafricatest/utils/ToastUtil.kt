package menolla.co.za.itsi_test.utils

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import nunens.co.za.simfyafricatest.App
import nunens.co.za.simfyafricatest.R


class ToastUtil {
    companion object {
        fun toast(ctx: Context, message: String, durationSeconds: Int,
                  gravity: Int) {
            App.vb!!.vibrate(30)
            val inf = ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val main = inf.inflate(R.layout.toast,
                    null) as LinearLayout

            val msg = main.findViewById<View>(R.id.txtTOASTMessage) as TextView
            msg.text = message

            val toast = CustomToast(ctx, durationSeconds)
            toast.setGravity(gravity, 0, 0)
            toast.view = main
            toast.show()
        }

        fun toast(ctx: Context, message: String) {
            App.vb!!.vibrate(30)
            val inf = ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val main = inf.inflate(R.layout.toast, null) as LinearLayout

            val msg = main.findViewById<View>(R.id.txtTOASTMessage) as TextView
            msg.text = message

            val toast = CustomToast(ctx, 3)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = main
            toast.show()
        }

        fun errorToast(ctx: Context, message: String) {
            App.vb!!.vibrate(50)
            val inf = ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val main = inf.inflate(R.layout.toast_error, null) as LinearLayout

            val msg = main.findViewById<View>(R.id.txtTOASTMessage) as TextView
            msg.text = message

            val toast = Toast(ctx)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = main
            toast.show()
            Log.e("ToastUtil", message)
        }

        fun noNetworkToast(ctx: Context) {
            App.vb!!.vibrate(50)
            val inf = ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val main = inf.inflate(R.layout.toast_error, null) as LinearLayout

            val msg = main.findViewById<View>(R.id.txtTOASTMessage) as TextView
            msg.text = "Network not available. Please check your network settings and your reception signal.\n" + "Please try again."

            val toast = Toast(ctx)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = main
            toast.show()

        }
    }
}
