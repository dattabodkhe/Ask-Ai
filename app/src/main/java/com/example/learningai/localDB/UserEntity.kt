package com.example.learningai.localDB

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey
    val uid: String,

    /* ---------- Basic ---------- */

    val role: String,

    val institutionType: String,

    val country: String,
    val state: String,


    /* ---------- College ---------- */

    val university: String,
    val college: String,

    val collegeEmail: String?,
    val collegeId: String?,
    val prn: String?,


    /* ---------- Private ---------- */

    val privateClass: String?,
    val studentId: String?
)
