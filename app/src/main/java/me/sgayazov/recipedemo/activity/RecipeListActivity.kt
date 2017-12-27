package me.sgayazov.recipedemo.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_recipe_list.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.recipe_list_layout.*
import me.sgayazov.recipedemo.R
import me.sgayazov.recipedemo.adapter.RecipeAdapter
import me.sgayazov.recipedemo.domain.Recipe
import me.sgayazov.recipedemo.domain.RecipeList
import me.sgayazov.recipedemo.fragment.RecipeDetailFragment
import me.sgayazov.recipedemo.presenter.RecipeListPresenter
import java.util.concurrent.TimeUnit

const val DEBOUNCE_SEARCH = 300L
const val MIN_SEARCH_LENGTH = 3

class RecipeListActivity : AppCompatActivity(), RecipeListView {

    //Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
    private var isTwoPane = false
    private val presenter = RecipeListPresenter(this)
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        setSupportActionBar(toolbar)
        toolbar.title = title
        isTwoPane = recipe_detail_container != null
        layoutManager = recipe_list.layoutManager
    }

    override fun startRecipesListLoad() {
        error_view.visibility = View.GONE
        error_retry_button.visibility = View.VISIBLE
        recipe_list.visibility = View.GONE
        loading_view.visibility = View.VISIBLE
    }

    override fun onError(error: Throwable) {
        loading_view.visibility = View.GONE
        error_view.visibility = View.VISIBLE
        error_reason.text = error.localizedMessage
        error_retry_button.setOnClickListener { presenter.loadData(search_view.text.toString()) }
    }

    override fun onDataReceived(data: RecipeList) {
        if (!data.recipes.isEmpty()) {
            recipe_list.adapter = RecipeAdapter(data.recipes, { onRecipeClick(it) })
            loading_view.visibility = View.GONE
            error_view.visibility = View.GONE
            recipe_list.visibility = View.VISIBLE
        } else {
            loading_view.visibility = View.GONE
            error_view.visibility = View.VISIBLE
            error_retry_button.visibility = View.GONE
            error_reason.text = resources.getString(R.string.nothing_found)
        }
    }

    private fun onRecipeClick(recipe: Recipe) {
        if (isTwoPane) {
            tabletOpenRecipeDetailed(recipe)
        } else {
            phoneOpenRecipeDetailed(recipe)
        }
    }

    private fun tabletOpenRecipeDetailed(recipe: Recipe) {
        val fragment = RecipeDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(RecipeDetailFragment.RECIPE_EXTRA, recipe)
                putBoolean(RecipeDetailFragment.IS_TABLET, true)
            }
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.recipe_detail_container, fragment)
                .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
        outState.putParcelable(RECIPE_LLM_EXTRA, recipe_list.layoutManager.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            presenter.onRestoreInstanceState(it)
            recipe_list.layoutManager.onRestoreInstanceState(it.getParcelable<Parcelable>(RECIPE_LLM_EXTRA))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.setSearchObservable(RxTextView
                .textChanges(search_view)
                .skip(1)
                .filter { it.length >= MIN_SEARCH_LENGTH }
                .debounce(DEBOUNCE_SEARCH, TimeUnit.MILLISECONDS)
                .map { it.toString() }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    private fun phoneOpenRecipeDetailed(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java).apply {
            putExtra(RecipeDetailFragment.RECIPE_EXTRA, recipe)
            putExtra(RecipeDetailFragment.IS_TABLET, false)
        }
        startActivity(intent)
    }

    companion object {
        const val RECIPE_LIST_EXTRA = "recipe_list_extra"
        const val RECIPE_LLM_EXTRA = "recipe_llm_extra"
    }
}

interface RecipeListView {
    fun onDataReceived(data: RecipeList)
    fun onError(error: Throwable)
    fun startRecipesListLoad()
}
