package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ExamProject
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.SpecificationRow
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    // Projects
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ExamProject>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: Long): Flow<ExamProject?>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectByIdDirect(id: Long): ExamProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ExamProject): Long

    @Update
    suspend fun updateProject(project: ExamProject)

    @Delete
    suspend fun deleteProject(project: ExamProject)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    // Matrix
    @Query("SELECT * FROM matrix_rows WHERE projectId = :projectId ORDER BY id ASC")
    fun getMatrixRows(projectId: Long): Flow<List<MatrixRow>>

    @Query("SELECT * FROM matrix_rows WHERE projectId = :projectId ORDER BY id ASC")
    suspend fun getMatrixRowsDirect(projectId: Long): List<MatrixRow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatrixRows(rows: List<MatrixRow>)

    @Query("DELETE FROM matrix_rows WHERE projectId = :projectId")
    suspend fun deleteMatrixRows(projectId: Long)

    // Specification
    @Query("SELECT * FROM specification_rows WHERE projectId = :projectId ORDER BY id ASC")
    fun getSpecRows(projectId: Long): Flow<List<SpecificationRow>>

    @Query("SELECT * FROM specification_rows WHERE projectId = :projectId ORDER BY id ASC")
    suspend fun getSpecRowsDirect(projectId: Long): List<SpecificationRow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecRows(rows: List<SpecificationRow>)

    @Query("DELETE FROM specification_rows WHERE projectId = :projectId")
    suspend fun deleteSpecRows(projectId: Long)

    // Questions
    @Query("SELECT * FROM questions WHERE projectId = :projectId ORDER BY id ASC")
    fun getQuestions(projectId: Long): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE projectId = :projectId ORDER BY id ASC")
    suspend fun getQuestionsDirect(projectId: Long): List<Question>

    @Query("SELECT * FROM questions ORDER BY id DESC")
    fun getAllBankQuestions(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long

    @Update
    suspend fun updateQuestion(question: Question)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Long)

    @Query("DELETE FROM questions WHERE projectId = :projectId")
    suspend fun deleteQuestionsForProject(projectId: Long)
}
