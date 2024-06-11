package com.marcelomaga.pokedex

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipe_table")
    fun getAllRecipes(): MutableList<Recipe>

    @Query("SELECT * FROM recipe_table WHERE id = :id ")
    fun getRecipeById(id: Int): Recipe
}