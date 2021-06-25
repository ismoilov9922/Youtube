package uz.pdp.youtube.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class VideoHome1(
    @PrimaryKey
    val videoId: String,
    val title: String,
    val description: String,
    val image: String,
)
