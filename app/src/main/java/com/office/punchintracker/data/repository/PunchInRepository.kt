/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker.data.repository

import com.office.punchintracker.data.local.PunchInDao
import com.office.punchintracker.data.local.PunchInEntity
import kotlinx.coroutines.flow.Flow

class PunchInRepository(private val dao: PunchInDao) {

    fun getAllPunchIns(userId: String): Flow<List<PunchInEntity>> {
        return dao.getAllPunchIns(userId)
    }

    suspend fun insertPunchIn(punchIn: PunchInEntity) {
        dao.insertPunchIn(punchIn)
    }

    suspend fun deleteAllPunchIns(userId: String) {
        dao.deleteAllPunchIns(userId)
    }
}