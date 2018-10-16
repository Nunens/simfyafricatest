package menolla.co.za.itsi_test.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast


class CustomToast(context: Context, internal var mDuration: Int) : Toast(context) {
    internal var mShowing = false


    /**
     * Set the time to show the toast for (in seconds)
     * @param seconds Seconds to display the toast
     */
    override fun setDuration(seconds: Int) {
        var seconds = seconds
        super.setDuration(Toast.LENGTH_SHORT)
        if (seconds < 2) seconds = 2 //Minimum
        mDuration = seconds
    }

    /**
     * Show the toast for the given time
     */
    override fun show() {
        super.show()
        if (mShowing) {
            return
        }

        mShowing = true
        val thisToast = this
        object : CountDownTimer(((mDuration - 2) * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                thisToast.show()
            }

            override fun onFinish() {
                thisToast.show()
                mShowing = false
            }

        }.start()
    }
}