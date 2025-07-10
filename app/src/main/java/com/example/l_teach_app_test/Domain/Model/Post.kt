package com.example.l_teach_app_test.Domain.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import java.util.Date

@Parcelize
data class Post(
    val id: String,
    val title: String,
    val text: String,
    val imageUrl: String,
    val sortOrder: Int,
    val date: Date
): Parcelable
