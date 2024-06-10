package com.marcelomaga.pokedex

import android.app.Application
import androidx.room.Room

class InstanceRoom: Application() {

    companion object {
        lateinit var database: Database
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "recipes.db"
        ).build()
    }
}