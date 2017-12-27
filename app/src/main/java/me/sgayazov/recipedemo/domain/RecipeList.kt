package me.sgayazov.recipedemo.domain

import android.os.Parcel
import android.os.Parcelable

data class RecipeList(val count: Int,
                      val recipes: List<Recipe>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.createTypedArrayList(Recipe.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(count)
        writeTypedList(recipes)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecipeList> = object : Parcelable.Creator<RecipeList> {
            override fun createFromParcel(source: Parcel): RecipeList = RecipeList(source)
            override fun newArray(size: Int): Array<RecipeList?> = arrayOfNulls(size)
        }
    }
}