package com.galal.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavoriteMovies(): List<FavoriteMovieEntity>

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun delete(movieId: Int)
}


/*@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertMovieInList(watchListModel: FavoriteMovieEntity)

    @Query("DELETE FROM watch_list_table WHERE mediaId =:mediaId")
    suspend fun removeFromList(mediaId: Int)

    @Query("DELETE FROM watch_list_table")
    suspend fun deleteList()

    @Query("SELECT EXISTS (SELECT 1 FROM watch_list_table WHERE mediaId = :mediaId)")
    suspend fun exists(mediaId: Int): Int

    @Query("SELECT * FROM watch_list_table ORDER BY addedOn DESC")
    fun getAllWatchListData(): Flow<List<FavoriteMovieEntity>>
}*/
