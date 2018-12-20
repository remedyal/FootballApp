package com.remedyal.footballapp.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.remedyal.footballapp.model.data.FavoriteEventData
import com.remedyal.footballapp.model.data.FavoriteTeamData
import com.remedyal.footballapp.R.string.*
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, ctx.getString(db_name), null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FavoriteEventData.TABLE_EVENT_FAVORITE, true,
            FavoriteEventData.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteEventData.EVENT_ID to TEXT + UNIQUE,
            FavoriteEventData.EVENT_DATE to TEXT,
            FavoriteEventData.EVENT_TIME to TEXT,
            FavoriteEventData.HOME_TEAM to TEXT,
            FavoriteEventData.AWAY_TEAM to TEXT,
            FavoriteEventData.HOME_SCORE to INTEGER,
            FavoriteEventData.AWAY_SCORE to INTEGER
        )

        db.createTable(
            FavoriteTeamData.TABLE_TEAM_FAVORITE, true,
            FavoriteTeamData.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteTeamData.TEAM_ID to TEXT + UNIQUE,
            FavoriteTeamData.TEAM_NAME to TEXT,
            FavoriteTeamData.TEAM_BADGE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteEventData.TABLE_EVENT_FAVORITE, true)
        db.dropTable(FavoriteTeamData.TABLE_TEAM_FAVORITE, true)
    }
}

val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)