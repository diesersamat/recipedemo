package me.sgayazov.recipedemo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_list_content.view.*
import me.sgayazov.recipedemo.R
import me.sgayazov.recipedemo.domain.Recipe

class RecipeAdapter(private val items: List<Recipe>,
                    private val callback: (recipe: Recipe) -> Unit) :
        RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Picasso.with(holder.icon.context).load(item.imageUrl).into(holder.icon);
        holder.title.text = item.title

        with(holder.itemView) {
            setOnClickListener({ callback(item) })
        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.icon
        val title: TextView = view.title
    }
}