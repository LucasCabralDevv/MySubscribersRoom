package com.lucascabral.mysubscribers.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lucascabral.mysubscribers.data.db.dao.SubscriberDAO
import com.lucascabral.mysubscribers.data.db.entity.SubscriberEntity

@Database(entities = [SubscriberEntity::class], version = 1)
abstract class SubscriberDatabase: RoomDatabase() {

    abstract val subscriberDAO: SubscriberDAO

    companion object {
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null

        fun getInstance(context: Context): SubscriberDatabase? {
            synchronized(this) {
                var instance: SubscriberDatabase? = INSTANCE
                if (instance == null) {
                    instance == Room.databaseBuilder(
                        context,
                        SubscriberDatabase::class.java,
                        "subscriber_db"
                    ).build()
                }
                return instance
            }
        }
    }
}