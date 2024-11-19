import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilderInstance {

    val api = "https://pixabay.com/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(api).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
