package com.mardana.respanimal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialModel(
    val id: String? = null,
    val title: String? = null,
    val picture: String? = null,
    val summary: String? = null,
    val detail: String? = null,
    val createdDate: Long? = null,
    val lastUpdatedDate: Long? = null,
): Parcelable
