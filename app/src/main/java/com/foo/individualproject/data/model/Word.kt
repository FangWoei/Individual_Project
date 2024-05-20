package com.foo.individualproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val meaning: String,
    val synonyms: String,
    val details: String,
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false
) {

}