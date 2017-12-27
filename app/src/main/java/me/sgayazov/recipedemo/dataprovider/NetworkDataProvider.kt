package me.sgayazov.recipedemo.dataprovider

import me.sgayazov.recipedemo.networking.RetrofitService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const private val BASE_URL = "http://food2fork.com/api/"
const private val API_KEY = "b549c4c96152e677eb90de4604ca61a2"

class NetworkDataProvider {

    private var apiService: RetrofitService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitService::class.java)

    fun getRecipeDetailed(recipeId: String) = apiService.getRecipeDetailed(API_KEY, recipeId)

    fun searchForRecipes(query: String, sort: Int? = null, page: Int? = null)
            = apiService.searchForRecipes(API_KEY, query, sort, page)
}