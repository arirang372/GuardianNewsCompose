package com.john.guardian.models


import android.os.Parcelable
import com.john.guardian.toDate
import com.john.guardian.formatTo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Field(
    val headline: String? = "",
    val standfirst: String? = "",
    val trailText: String? = "",
    val main: String? = "",
    val body: String? = "",
    val commentCloseDate: String? = "",
    val firstPublicationDate: String? = "",
    val lastModified: String? = "",
    val shortUrl: String? = "",
    val showInRelatedContent: Boolean,
    val thumbnail: String? = "",
    val isLive: Boolean,
    val bodyText: String? = ""
) : Comparable<Field>, Parcelable {
    override fun compareTo(other: Field) = 0
    fun lastModifiedFormatted() = lastModified?.toDate()?.formatTo("MMM dd, yyyy hh:mm a")
}