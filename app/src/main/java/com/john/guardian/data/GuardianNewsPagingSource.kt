package com.john.guardian.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.john.guardian.models.Article
import com.john.guardian.models.Section

private const val STARTING_PAGE_INDEX = 1
private const val DEFAULT_PAGE_SIZE = 5

class GuardianNewsPagingSource(
    private val service: GuardianNewsService,
    private val section: Section,
    private val pageType: String,
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response =
                section.sectionName?.let {
                    service.getArticles(
                        it,
                        page,
                        DEFAULT_PAGE_SIZE,
                        pageType
                    )
                }!!
            section.articles = response.response.results
            for (article in section.articles!!) {
                article.mostViewed = response.response.mostViewed
            }
            LoadResult.Page(
                data = response.response.results,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page + response.response.pageSize == response.response.startIndex) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}