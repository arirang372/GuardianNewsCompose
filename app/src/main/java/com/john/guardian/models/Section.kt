package com.john.guardian.models


import androidx.paging.PagingData
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class Section(
    @SerializedName("primary_id")
    val id: Int,
    @SerializedName("id") val sectionName: String? = "",
    val webTitle: String? = "",
    val webUrl: String? = "",
    val apiUrl: String? = "",
    var articles: List<Article>? = mutableListOf(),
    var pagerData: Flow<PagingData<Article>> = flow { PagingData.empty<Article>() }
) : Comparable<Section> {
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