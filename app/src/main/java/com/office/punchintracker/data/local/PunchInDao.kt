/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PunchInDao {
    @Insert
    suspend fun insertPunchIn(punchIn: PunchInEntity)

    @Query("SELECT * FROM punch_ins WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllPunchIns(userId: String): Flow<List<PunchInEntity>>

    @Query("DELETE FROM punch_ins WHERE userId = :userId")
    suspend fun deleteAllPunchIns(userId: String)
}