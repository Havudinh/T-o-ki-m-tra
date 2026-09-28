package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.ExamProject
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.SpecificationRow

@Database(
    entities = [
        ExamProject::class,
        MatrixRow::class,
        SpecificationRow::class,
        Question::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExamTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun examDao(): ExamDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "thcs_long_xuyen_exam.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
