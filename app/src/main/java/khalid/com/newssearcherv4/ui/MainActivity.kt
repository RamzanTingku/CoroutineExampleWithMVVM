package khalid.com.newssearcherv4.ui

import android.content.Intent
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

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModelFactory = NewsViewModelFactory()
        newsViewModel = ViewModelProviders.of(this@MainActivity, viewModelFactory).get(NewsViewModel::class.java)

        newsViewModel.viewState.observe(this, Observer { handleState(it) })
        newsViewModel.getLatestNews("Nigeria", "publishedAt")
            ?.observe(this, Observer { setData(it) })

        textview.setOnClickListener { startActivity(Intent(this, NextActivity::class.java)) }
    }

    private fun setData(latestNew: LatestNews?) {
        textview.text = latestNew.toString()
    }

    private fun handleState(viewState: Status) {
        if(viewState == Status.LOADING){ textview.text = viewState.name; Log.d(TAG,  viewState.name)}else{}
        if(viewState == Status.SUCCESS){ Log.d(TAG,  viewState.name)}else{}
        if(viewState == Status.EMPTY_DATA){ textview.text = viewState.name; Log.d(TAG,  viewState.name)}else{}
        if(viewState == Status.NO_NETWORK){ textview.text = viewState.name; Log.d(TAG,  viewState.name)}else{}
        if(viewState == Status.SERVER_ERROR){ textview.text = viewState.name; Log.d(TAG,  viewState.name)}else{}
    }


    override fun onDestroy() {
        super.onDestroy()
        newsViewModel.cancelRequests()
    }
}
