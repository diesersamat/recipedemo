package me.sgayazov.recipedemo.dataprovider

import io.reactivex.Observable
import me.sgayazov.recipedemo.domain.RecipeList
import me.sgayazov.recipedemo.domain.RecipeWrapper

//        TODO("not implemented") //implement database provider

class CacheDataProvider {

    fun getRecipeDetailed(recipeId: String): Observable<RecipeWrapper> = Observable.empty()

    fun searchForRecipes(query: String, sort: Int? = null, page: Int? = null): Observable<RecipeList> = Observable.empty()

    fun saveRecipeList(it: RecipeList, query: String) {
    }

    fun saveRecipeDetailed(it: RecipeWrapper) {
    }
}