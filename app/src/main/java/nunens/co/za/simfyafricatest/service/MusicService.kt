package nunens.co.za.simfyafricatest.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import nunens.co.za.simfyafricatest.App

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_PLAY = "nunens.co.za.simfyafricatest.service.action.PLAY"
private const val ACTION_STOP = "nunens.co.za.simfyafricatest.service.action.STOP"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "file"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class MusicService : IntentService("MusicService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY -> {
                val param1 = intent.getIntExtra(EXTRA_PARAM1, 0)
                handleActionPlay(param1)
            }
            ACTION_STOP -> {
                val param1 = intent.getIntExtra(EXTRA_PARAM1, 0)
                handleActionStop(param1)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionPlay(param1: Int) {
        val mp = MediaPlayer.create(applicationContext, param1)
        App.vb!!.vibrate(1000)
        mp.start()
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionStop(param1: Int) {
        TODO("Handle action Stop")
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionPlay(context: Context, param1: Int) {
            val intent = Intent(context, MusicService::class.java).apply {
                action = ACTION_PLAY
                putExtra(EXTRA_PARAM1, param1)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionStop(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MusicService::class.java).apply {
                action = ACTION_STOP
                putExtra(EXTRA_PARAM1, param1)
            }
            context.startService(intent)
        }
    }
}
