package nunens.co.za.simfyafricatest.database.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class TaskOneModel(@PrimaryKey
                        var id: Int = 0) {
    var name: String = ""
    var stats: String = ""
    var weight: Int = 0
    var height: Int = 0
    var abilities: String = ""
    var moves: String = ""
    var types: String = ""
}