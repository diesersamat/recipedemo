package me.sgayazov.recipedemo.activity

import android.R.id.home
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import me.sgayazov.recipedemo.R
import me.sgayazov.recipedemo.domain.Recipe
import me.sgayazov.recipedemo.fragment.RecipeDetailFragment

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        setSupportActionBar(detail_toolbar)
        detail_toolbar.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RecipeDetailFragment.RECIPE_EXTRA, intent.getParcelableExtra<Recipe>(RecipeDetailFragment.RECIPE_EXTRA))
                    putBoolean(RecipeDetailFragment.IS_TABLET, intent.getBooleanExtra(RecipeDetailFragment.IS_TABLET, false))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                home -> {
                    NavUtils.navigateUpTo(this, Intent(this, RecipeListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
