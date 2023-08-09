package com.mardana.respanimal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionModel(
    val id: String? = null,
    val question: String? = null,
    val option: Map<String, String>? = null,
    val answer: String? = null,
    val createdDate: Long? = null,
    val lastUpdatedDate: Long? = null,
): Parcelable
