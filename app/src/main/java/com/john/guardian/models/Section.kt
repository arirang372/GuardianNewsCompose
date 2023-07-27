package com.john.guardian.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Section(
    @SerializedName("primary_id")
    val id: Int,
    @SerializedName("id") val sectionName: String? = "",
    val webTitle: String? = "",
    val webUrl: String? = "",
    val apiUrl: String? = "",
    var articles: List<Article>? = mutableListOf()
) : Comparable<Section>, Parcelable {
    override fun compareTo(other: Section) = 0

    @ExperimentalStdlibApi
    fun titleCapitalize(input: String): String {
        val words: List<String> = input.split("-")
        var newWord = ""
        for (single in words) {
            var newInput = single.substring(0, 1).uppercase() + single.substring(1).lowercase()
            newWord += "$newInput "
        }
        return newWord
    }
}