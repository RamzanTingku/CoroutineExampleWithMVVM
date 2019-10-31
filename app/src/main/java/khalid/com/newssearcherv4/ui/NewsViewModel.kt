package khalid.com.newssearcherv4.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import khalid.com.newssearcherv4.network.NewsApiService
import khalid.com.newssearcherv4.network.models.LatestNews
import khalid.com.newssearcherv4.repositories.NewsRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by  on 4/13/2019.
 */
class NewsViewModel : ViewModel(){
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val newsRepository : NewsRepo = NewsRepo(NewsApiService.newsApi)
    val newsLiveData = MutableLiveData<LatestNews>()

    fun getLatestNews() {
        scope.launch {
            val latestNew = newsRepository.getLatestNews()
            newsLiveData.postValue(latestNew)
        }
    }
    fun cancelRequests() = coroutineContext.cancel()
}