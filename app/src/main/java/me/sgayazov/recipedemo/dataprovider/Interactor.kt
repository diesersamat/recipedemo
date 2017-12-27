package me.sgayazov.recipedemo.dataprovider

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.sgayazov.recipedemo.domain.RecipeList
import me.sgayazov.recipedemo.domain.RecipeWrapper

class Interactor {

    private var cacheDataProvider: CacheDataProvider = CacheDataProvider()
    private var networkDataProvider: NetworkDataProvider = NetworkDataProvider()

    //todo maybe support paging of recipes later
    fun searchForRecipes(query: String, sort: Int? = null, page: Int? = null): Single<RecipeList> {
        return Observable
                .concat(cacheDataProvider.searchForRecipes(query, sort, page),
                        networkDataProvider.searchForRecipes(query, sort, page)
                                .doOnNext({ cacheDataProvider.saveRecipeList(it, query) }))
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getRecipeDetailed(recipeId: String): Single<RecipeWrapper> {
        return Observable
                .concat(cacheDataProvider.getRecipeDetailed(recipeId),
                        networkDataProvider.getRecipeDetailed(recipeId)
                                .doOnNext({ cacheDataProvider.saveRecipeDetailed(it) }))
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}