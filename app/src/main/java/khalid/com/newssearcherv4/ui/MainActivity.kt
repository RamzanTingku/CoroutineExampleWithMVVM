package khalid.com.newssearcherv4.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import khalid.com.newssearcherv4.R
import khalid.com.newssearcherv4.network.models.LatestNews
import khalid.com.newssearcherv4.ui.Constant.DATA_SHOWN_STATE
import khalid.com.newssearcherv4.ui.Constant.NO_DATA_SHOWN_STATE
import khalid.com.newssearcherv4.ui.Constant.NO_INTERNET_SHOWN_STATE
import khalid.com.newssearcherv4.ui.Constant.PROGRESS_DIALOG_SHOWN_STATE
import khalid.com.newssearcherv4.ui.Constant.SERVER_ERROR_SHOWN_STATE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val isNetworkConnected = true
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModelFactory = NewsViewModelFactory()
        newsViewModel = ViewModelProviders.of(this@MainActivity, viewModelFactory).get(NewsViewModel::class.java)

        getData()
    }

    private fun getData() {

        handleState(PROGRESS_DIALOG_SHOWN_STATE)
        if(!isNetworkConnected){
            handleState(NO_INTERNET_SHOWN_STATE)
            return
        }

        newsViewModel.getLatestNews()
        newsViewModel.newsLiveData.observe(this, Observer {latestNew ->
            if(latestNew != null){
                if(!latestNew.articles.isEmpty()){
                    handleState(DATA_SHOWN_STATE)
                    showData(latestNew)
                }else{
                    handleState(NO_DATA_SHOWN_STATE)
                }
            }else{
                handleState(SERVER_ERROR_SHOWN_STATE)
            }
        })
    }

    private fun showData(latestNew: LatestNews?) {
        textview.text = latestNew.toString()
    }

    private fun handleState(viewState: String) {
        when (viewState){
            PROGRESS_DIALOG_SHOWN_STATE -> {
                showProgressDialog()
            }
            DATA_SHOWN_STATE -> {
                showMainView()
            }
            NO_DATA_SHOWN_STATE -> {
                showNoDataFound()
            }
            NO_INTERNET_SHOWN_STATE -> {
                showNetworkDisconnected()
            }
            SERVER_ERROR_SHOWN_STATE -> {
                showServerError()
            }
            else -> showProgressDialog()
        }
    }

    private fun showMainView() {

    }

    private fun showServerError() {

    }

    private fun showNoDataFound() {

    }

    private fun showNetworkDisconnected() {

    }

    private fun showProgressDialog() {

    }

    override fun onDestroy() {
        super.onDestroy()
        newsViewModel.cancelRequests()
    }
}
