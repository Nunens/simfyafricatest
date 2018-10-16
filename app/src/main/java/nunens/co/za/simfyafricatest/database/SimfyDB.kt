package nunens.co.za.simfyafricatest.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import nunens.co.za.simfyafricatest.database.dao.TaskOneDAO
import nunens.co.za.simfyafricatest.database.dao.ThirdTaskDAO
import nunens.co.za.simfyafricatest.database.model.SecondTaskListModel
import nunens.co.za.simfyafricatest.database.model.TaskOneModel

@Database(entities = [(TaskOneModel::class), (SecondTaskListModel::class)], version = 1)
abstract class SimfyDB : RoomDatabase() {
    //abstract method for accessing our user data access object
    abstract fun taskOneDAO(): TaskOneDAO

    abstract fun thirdTaskDAO(): ThirdTaskDAO

    //create or get the db
    companion object {
        var INSTANCE: SimfyDB? = null
        fun getDatabase(context: Context): SimfyDB {
            if (INSTANCE == null) {
                synchronized(SimfyDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context,
                                SimfyDB::class.java!!, "simfy-db")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .addCallback(sRoomDatabaseCallback)
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}