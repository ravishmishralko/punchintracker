/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "punch_ins")
data class PunchInEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val userId: String
)