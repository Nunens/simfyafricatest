package nunens.co.za.simfyafricatest.database.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class SecondTaskListModel(@PrimaryKey
                               var id: String = "") {
    var title: String = ""
    var description: String = ""
    var image: String = ""
}