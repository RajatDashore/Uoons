package com.uoons.india.ui.saas.adapter


import RetrofitBuilderInstance
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.saas.adapter.getApiData.MyInterface
import com.uoons.india.ui.saas.adapter.model.CatImagesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivitySaas : AppCompatActivity() {

    private var list: ArrayList<CatImagesItem> = ArrayList()
    private var saasMainAdapter = SaasMainAdapter()
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_saas)

        setMainRecyclerView()
        //  setLowerRecyclerView()
        getApiDataImages()
        backButton = findViewById(R.id.saasBackButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setMainRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.SaasMainAdapter)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = saasMainAdapter
    }

    private fun getApiDataImages() {
        val retroftBuilder = RetrofitBuilderInstance().getInstance().create(MyInterface::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retroftBuilder.getImages()
                response!!.let {
                    list.addAll(it)
                    CoroutineScope(Dispatchers.Main).launch {
                        saasMainAdapter.setUpperData(this@MainActivitySaas, list)
                        saasMainAdapter.setLowerData(list)
                    }
                }
            } catch (e: HttpException) {
                Log.e("getApiDataImages", "HTTP Error: ${e.code()}, ${e.message()}")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        this@MainActivitySaas,
                        "HTTP Error: ${e.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("getApiDataImages", "Exception: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        this@MainActivitySaas,
                        "Unexpected error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
