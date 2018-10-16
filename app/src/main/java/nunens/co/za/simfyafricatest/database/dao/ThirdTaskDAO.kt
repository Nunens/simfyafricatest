package nunens.co.za.simfyafricatest.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import nunens.co.za.simfyafricatest.database.model.SecondTaskListModel

@Dao
interface ThirdTaskDAO {

    @Query("Delete FROM SecondTaskListModel")
    fun deleteAll()

    @Query("Delete FROM SecondTaskListModel where id = :id")
    fun delete(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskOneModel: SecondTaskListModel)
}