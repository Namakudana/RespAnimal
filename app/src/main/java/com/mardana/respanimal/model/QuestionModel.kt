package com.mardana.respanimal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionModel(
    val id: String,
    val question: String,
    val option1: Map<String, String>,
    val option2: Map<String, String>,
    val option3: Map<String, String>,
    val option4: Map<String, String>,
    val answer: String,
    val createdDate: Long,
    val lastUpdatedDate: Long,
): Parcelable
