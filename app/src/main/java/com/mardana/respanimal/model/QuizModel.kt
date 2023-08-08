package com.mardana.respanimal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizModel(
    val id: String,
    val name: String,
    val level: Int,
    val createdDate: Long,
    val lastUpdatedDate: Long,
    val questions: Map<String, QuestionModel>,
): Parcelable
