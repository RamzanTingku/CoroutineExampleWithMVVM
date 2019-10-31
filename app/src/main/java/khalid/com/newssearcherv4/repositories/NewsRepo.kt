package khalid.com.newssearcherv4.repositories

import khalid.com.newssearcherv4.network.NewsApiInterface
import khalid.com.newssearcherv4.network.models.Article
import khalid.com.newssearcherv4.network.models.LatestNews

/**
 * Created by  on 4/13/2019.
 */
class NewsRepo(private val apiInterface: NewsApiInterface) : BaseRepository() {

    suspend fun getLatestNews() : LatestNews?{

        val latestNews = safeApiCall(
            call = { apiInterface.fetchLatestNewsAsync("Nigeria", "publishedAt").await() },
            error = "Error fetching news"
        )

        return latestNews
    }
}