package nunens.co.za.simfyafricatest

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.support.multidex.MultiDex
import com.facebook.stetho.Stetho
import nunens.co.za.simfyafricatest.database.SimfyDB

class App : Application() {
    companion object {
        var db: SimfyDB? = null
        var context: Context? = null
        var vb: Vibrator? = null
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate() {
        super.onCreate()
        App.context = applicationContext

        //Room Database initialization
        App.db = SimfyDB.getDatabase(applicationContext)

        //Initialize Vibrator service
        vb = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        //To avoid android.os.FileUriExposedException
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        //Initialize Stetho to use with Google chrome to see SQLite DB during Runtime
        Stetho.initializeWithDefaults(this)
        Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //Initialize Multidex
        MultiDex.install(this)
    }
}