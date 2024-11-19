package com.uoons.india.ui.saas.adapter


import RetrofitBuilderInstance
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.saas.adapter.getApiData.MyInterface
import com.uoons.india.ui.saas.adapter.model.PixabayResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivitySaas : AppCompatActivity() {
    val baseURL =
        "https://pixabay.com/" // Fake API for Testing only
    private val list = ArrayList<PixabayResponse>()
    private val list2 = ArrayList<ArrayList<PixabayResponse>>()
    private var px: PixabayResponse? = null
    private var scrolling = false
    private var currentItem = 0
    private var totalItem = 0
    private var scrollOutItem = 0
    private var saasMainAdapter = SaasMainAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_saas)
        getApiDataImages()
        SetAdapterMain()
    }

    private fun SetAdapterMain() {
        val recyclerView = findViewById<RecyclerView>(R.id.SaasMainAdapter)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = saasMainAdapter
        saasMainAdapter.setData(this, list)
        recyclerView.layoutManager =
            layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = layoutManager.childCount
                totalItem = layoutManager.itemCount
                scrollOutItem = layoutManager.findFirstVisibleItemPosition()
                if (scrolling) {
                    if (currentItem + scrollOutItem == totalItem) {
                        scrolling = false

                    }
                }
            }


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true
                    getApiDataImages()
                }
            }
        })
    }


    private fun getApiDataImages() {
        val retroftBuilder =
            RetrofitBuilderInstance().getInstance().create(MyInterface::class.java)
        try {
            GlobalScope.launch(Dispatchers.Default) {
                val result = retroftBuilder.getImages()
                if (result.isSuccessful) {
                    result.body()?.let {
                        list.add(it)
                        Log.d("result", "one: $list")
                    }
                    Log.d("result", "two: $list")
                    Log.d("result", "three: ${result.body()!!.fullHDURL}")
                    // saasMainAdapter.notifyDataSetChanged()
                }

            }
        } catch (e: Exception) {
            Log.d("result", "Error: ${e.message}")
        }

    }
}
