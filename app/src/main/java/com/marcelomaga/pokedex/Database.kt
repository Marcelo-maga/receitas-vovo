package com.marcelomaga.pokedex

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun recipeDAO(): RecipeDAO
}
