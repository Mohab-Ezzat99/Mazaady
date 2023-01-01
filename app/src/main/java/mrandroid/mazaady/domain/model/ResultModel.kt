package mrandroid.mazaady.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultModel(
    val title: String,
    val value: String
) : Parcelable
