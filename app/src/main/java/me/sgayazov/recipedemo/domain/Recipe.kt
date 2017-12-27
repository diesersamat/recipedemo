package me.sgayazov.recipedemo.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Recipe(val publisher: String,
                  val title: String,
                  @SerializedName("f2f_url") val f2fUrl: String,
                  @SerializedName("source_url") val sourceUrl: String,
                  @SerializedName("recipe_id") val recipeId: String,
                  @SerializedName("image_url") val imageUrl: String,
                  @SerializedName("social_rank") val socialRank: Double,
                  @SerializedName("publisher_url") val publisherUrl: String,
                  val ingredients: List<String>?) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readString(),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(publisher)
        writeString(title)
        writeString(f2fUrl)
        writeString(sourceUrl)
        writeString(recipeId)
        writeString(imageUrl)
        writeDouble(socialRank)
        writeString(publisherUrl)
        writeStringList(ingredients)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Recipe> = object : Parcelable.Creator<Recipe> {
            override fun createFromParcel(source: Parcel): Recipe = Recipe(source)
            override fun newArray(size: Int): Array<Recipe?> = arrayOfNulls(size)
        }
    }
}