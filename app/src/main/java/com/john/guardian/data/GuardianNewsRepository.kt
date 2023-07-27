package com.john.guardian.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.john.guardian.models.Article
import com.john.guardian.models.Section
import kotlinx.coroutines.flow.Flow

class GuardianNewsRepository(private val service: GuardianNewsService) {

    suspend fun getSections(): GuardianServiceResponse<Section> {
        return service.getSections()
    }

    suspend fun getArticles(
        sectionId: String,
        articleType: String = ""
    ): GuardianServiceResponseResult<List<Article>> {
        val serviceResponse = service.getArticles(
            sectionId,
            1, 10, articleType, "all", "all", true
        )
        val articles: List<Article> = serviceResponse.response.results
        articles.forEach { article ->
            article.mostViewed = serviceResponse.response.mostViewed
        }
        return GuardianServiceResponseResult.success(articles)
    }

    fun getSectionNewsArticle(
        section: Section,
        pageType: String
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = DEFAULT_PAGE_SIZE),
            pagingSourceFactory = {
                GuardianNewsPagingSource(service, section, pageType)
            }
        ).flow
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }
}