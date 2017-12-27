package me.sgayazov.recipedemo.networking

import io.reactivex.Observable
import me.sgayazov.recipedemo.domain.RecipeList
import me.sgayazov.recipedemo.domain.RecipeWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("search")
    fun searchForRecipes(@Query("key") key: String,
                         @Query("q") query: String,
                         @Query("sort") sort: Int? = null,
                         @Query("page") page: Int? = null): Observable<RecipeList>

    @GET("get")
    fun getRecipeDetailed(@Query("key") key: String,
                          @Query("rId") recipeId: String): Observable<RecipeWrapper>
}