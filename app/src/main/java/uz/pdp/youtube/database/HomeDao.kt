package uz.pdp.youtube.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<VideoHome>)

    @Query("Select * from VideoHome")
    fun getVideoHome(): List<VideoHome>



}