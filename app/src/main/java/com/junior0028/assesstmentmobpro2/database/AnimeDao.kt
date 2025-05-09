package com.junior0028.assesstmentmobpro2.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.junior0028.assesstmentmobpro2.model.Anime
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Insert
    suspend fun insert(anime: Anime)

    @Update
    suspend fun update(anime: Anime)

    @Query("SELECT * FROM anime WHERE isDeleted = 0 ORDER BY title ASC")
    fun getAnime(): Flow<List<Anime>>

    @Query("SELECT * FROM anime WHERE isDeleted = 1 ORDER BY deletedDate DESC")
    fun getDeletedAnime(): Flow<List<Anime>>

    @Query("SELECT * FROM anime WHERE id = :id")
    suspend fun getAnimeById(id: Long): Anime?

    @Query("UPDATE anime SET isDeleted = 1, deletedDate = :timestamp WHERE id = :id")
    suspend fun moveToRecycleBin(id: Long, timestamp: Long)

    @Query("UPDATE anime SET isDeleted = 0, deletedDate = 0 WHERE id = :id")
    suspend fun restoreFromRecycleBin(id: Long)

    @Query("DELETE FROM anime WHERE id = :id")
    suspend fun permanentlyDeleteById(id: Long)

    @Query("DELETE FROM anime WHERE isDeleted = 1")
    suspend fun emptyRecycleBin()
}