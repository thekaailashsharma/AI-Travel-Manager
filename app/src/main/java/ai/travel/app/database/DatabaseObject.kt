package ai.travel.app.database


import ai.travel.app.database.travel.TripsDao
import ai.travel.app.database.travel.TripsEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [TripsEntity::class], version = 2)
@TypeConverters(ArrayListConverter::class)
abstract class DatabaseObject : RoomDatabase() {

    abstract fun tripsDao(): TripsDao

    companion object {
        @Volatile
        private var Instance: DatabaseObject? = null

        fun getInstance(context: Context): DatabaseObject {
            synchronized(this) {
                if (Instance == null) {
                    Instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseObject::class.java,
                            "TravelDatabase"
                        ).build()
                }
            }
            return Instance!!
        }
    }
}
