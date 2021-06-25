package uz.pdp.youtube.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeDao1 {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videoHome1: VideoHome1)

    @Query("Select * from VideoHome1")
    fun getVideoHome1(): List<VideoHome1>

}