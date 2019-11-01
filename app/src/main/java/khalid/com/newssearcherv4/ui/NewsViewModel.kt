package khalid.com.newssearcherv4.ui

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
    private var isNetworkDisconnected : Boolean = false
    private var parentJob = Job()
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val newsRepository : NewsRepo = NewsRepo(NewsApiService.newsApi)
    val viewState = MutableLiveData<Status>()

    fun getLatestNews(query: String, sort: String) : MutableLiveData<LatestNews>?{
        setViewState(Status.LOADING)
        if(isNetworkDisconnected) { setViewState(Status.NO_NETWORK); return null}

        val newsLiveData = MutableLiveData<LatestNews>()
        parentJob = scope.launch {
            val result = newsRepository.getLatestNews(query, sort)
            if(result != null && result.status == "ok"){
                newsLiveData.postValue(result)
                setViewState(Status.SUCCESS)
            }else{
                setViewState(Status.EMPTY_DATA)
            }
        }

        /*if(parentJob.isCompleted){
            parentJob.invokeOnCompletion {it}
        }*/

        return newsLiveData
    }

    fun setViewState(status: Status) {
        viewState.postValue(status)
    }
    fun cancelRequests() = coroutineContext.cancel()
}