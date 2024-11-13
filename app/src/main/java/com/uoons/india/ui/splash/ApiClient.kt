package com.uoons.india.ui.splash
 import com.google.gson.GsonBuilder
 import okhttp3.Interceptor
import okhttp3.OkHttpClient
 import org.lsposed.lsparanoid.Obfuscate
 import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Obfuscate
object ApiClient {
    var BASE_URL = "https://uoons.com/"

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("Channel-Code","ANDROID")
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }.build()

    fun create() : ApiInterface {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        return retrofit.create(ApiInterface::class.java)

    }

    /*private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit? {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original: Request = chain.request()
                chain.proceed(
                    original.newBuilder()
                        .method(original.method, original.body)
                        .header("Channel-Code","ANDROID")
                        .build()
                )
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder().setLenient().create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }
        return retrofit
    }

    fun getAPIService(): ApiInterface {
        return ApiClient.getClient()!!.create(ApiInterface::class.java)
    }*/


}