package me.sgayazov.recipedemo.presenter

import me.sgayazov.recipedemo.domain.RecipeWrapper
import me.sgayazov.recipedemo.fragment.RecipeDetailView

class RecipeDetailPresenter constructor(private val view: RecipeDetailView) : BasePresenter() {

    fun loadData(recipeId: String) {
        addSubscription(interactor.getRecipeDetailed(recipeId).subscribe({ t1: RecipeWrapper?, t2: Throwable? ->
            t2?.let { view.onError(it) }
            t1?.let { view.onDetailedRecipeReceived(it.recipe) }
        }))
    }
}