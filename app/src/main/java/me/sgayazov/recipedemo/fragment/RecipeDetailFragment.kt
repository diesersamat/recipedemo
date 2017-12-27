package me.sgayazov.recipedemo.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.recipe_detail.*
import kotlinx.android.synthetic.main.recipe_detail.view.*
import me.sgayazov.recipedemo.R
import me.sgayazov.recipedemo.domain.Recipe
import me.sgayazov.recipedemo.presenter.RecipeDetailPresenter


class RecipeDetailFragment : Fragment(), RecipeDetailView {

    private val presenter = RecipeDetailPresenter(this)

    private var recipe: Recipe? = null
    private var isTablet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { restoreData(it) }
        arguments?.let {
            restoreData(it)
            arguments = null
        }
    }

    private fun restoreData(it: Bundle) {
        if (it.containsKey(RECIPE_EXTRA)) {
            recipe = it.getParcelable(RECIPE_EXTRA)
        }
        isTablet = it.getBoolean(IS_TABLET)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.recipe_detail, container, false)

        recipe?.let { recipe ->
            if (!isTablet) {
                Picasso.with(context).load(recipe.imageUrl).into(activity?.header_image)
                rootView.image.visibility = View.GONE
            } else {
                Picasso.with(context).load(recipe.imageUrl).into(rootView.image)
            }
            activity?.toolbar_layout?.title = recipe.title
            rootView.view_instructions.setOnClickListener { openLink(recipe.f2fUrl) }
            rootView.view_original.setOnClickListener { openLink(recipe.sourceUrl) }
            rootView.rank_text.text = getString(R.string.social_rank, recipe.socialRank)
            rootView.publisher_text.text = getString(R.string.publisher, recipe.publisher)
            rootView.publisher_text.setOnClickListener { openLink(recipe.publisherUrl) }
        }
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe?.let { recipe ->
            if (recipe.ingredients != null) {
                updateIngredientsList(recipe.ingredients)
            } else {
                loadIngredientsList()
            }
        }
    }

    override fun onError(error: Throwable) {
        loading_view.visibility = View.GONE
        error_view.visibility = View.VISIBLE
        error_reason.text = error.localizedMessage
        error_retry_button.setOnClickListener { loadIngredientsList() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(RECIPE_EXTRA, recipe)
        outState.putBoolean(IS_TABLET, isTablet)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    private fun loadIngredientsList() {
        recipe?.let { presenter.loadData(it.recipeId) }
        error_view.visibility = View.GONE
        ingredients_list.visibility = View.GONE
        loading_view.visibility = View.VISIBLE
    }

    override fun onDetailedRecipeReceived(data: Recipe) {
        recipe = data
        data.ingredients?.let { updateIngredientsList(it) }
        error_view.visibility = View.GONE
        ingredients_list.visibility = View.VISIBLE
        loading_view.visibility = View.GONE
    }

    private fun updateIngredientsList(ingredients: List<String>) {
        ingredients.forEach {
            val child = layoutInflater.inflate(R.layout.ingredients_list_content, ingredients_list, false)
            (child as TextView).text = Html.fromHtml(it)
            ingredients_list.addView(child)
        }

    }

    private fun openLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    companion object {
        const val RECIPE_EXTRA = "recipe_extra"
        const val IS_TABLET = "is_tablet"
    }
}

interface RecipeDetailView {
    fun onDetailedRecipeReceived(data: Recipe)
    fun onError(error: Throwable)
}