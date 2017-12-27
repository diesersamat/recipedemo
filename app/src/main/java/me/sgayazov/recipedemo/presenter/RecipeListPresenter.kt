package me.sgayazov.recipedemo.presenter

import android.os.Bundle
import io.reactivex.Observable
import me.sgayazov.recipedemo.activity.RecipeListActivity
import me.sgayazov.recipedemo.activity.RecipeListActivity.Companion.RECIPE_LIST_EXTRA
import me.sgayazov.recipedemo.activity.RecipeListView
import me.sgayazov.recipedemo.domain.RecipeList

class RecipeListPresenter constructor(private val view: RecipeListView) : BasePresenter() {

    private var data: RecipeList? = null

    fun loadData(query: String) {
        view.startRecipesListLoad()
        addSubscription(interactor.searchForRecipes(query).subscribe({ t1: RecipeList?, t2: Throwable? ->
            t2?.let { view.onError(it) }
            t1?.let { onDataReceived(it) }
        }))
    }

    private fun onDataReceived(it: RecipeList) {
        data = it
        view.onDataReceived(it)
    }

    fun setSearchObservable(observable: Observable<String>) {
        addSubscription(observable.subscribe({ loadData(it) }))
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(RecipeListActivity.RECIPE_LIST_EXTRA, data)
    }

    fun onRestoreInstanceState(it: Bundle) {
        it.getParcelable<RecipeList>(RECIPE_LIST_EXTRA)?.let { onDataReceived(it) }
    }
}