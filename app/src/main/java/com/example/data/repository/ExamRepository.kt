package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.DefaultCurriculumData
import com.example.data.model.CognitiveLevel
import com.example.data.model.ExamProject
import com.example.data.model.ExamVariant
import com.example.data.model.GradeLevel
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.data.model.SpecificationRow
import com.example.data.model.SubjectType
import com.example.data.model.ValidationAuditItem
import com.example.data.remote.GeminiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ExamRepository(private val context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.examDao()

    val allProjects: Flow<List<ExamProject>> = dao.getAllProjects()
    val allBankQuestions: Flow<List<Question>> = dao.getAllBankQuestions()

    suspend fun ensureDefaultDataLoaded() = withContext(Dispatchers.IO) {
        val currentList = dao.getAllProjects().firstOrNull()
        if (currentList.isNullOrEmpty()) {
            val sampleProject = DefaultCurriculumData.getDefaultProject()
            val projectId = dao.insertProject(sampleProject)

            val matrixRows = DefaultCurriculumData.getSampleMatrix(projectId)
            dao.insertMatrixRows(matrixRows)

            val specRows = DefaultCurriculumData.getSampleSpecifications(projectId)
            dao.insertSpecRows(specRows)

            val questions = DefaultCurriculumData.getSampleQuestions(projectId)
            dao.insertQuestions(questions)
        }
    }

    fun getProject(id: Long): Flow<ExamProject?> = dao.getProjectById(id)
    fun getMatrixRows(projectId: Long): Flow<List<MatrixRow>> = dao.getMatrixRows(projectId)
    fun getSpecRows(projectId: Long): Flow<List<SpecificationRow>> = dao.getSpecRows(projectId)
    fun getQuestions(projectId: Long): Flow<List<Question>> = dao.getQuestions(projectId)

    suspend fun createNewProject(
        title: String,
        subject: SubjectType,
        grade: GradeLevel,
        semester: String,
        schoolYear: String,
        durationMinutes: Int,
        totalPoints: Double,
        bookSeries: String,
        scopeDescription: String
    ): Long = withContext(Dispatchers.IO) {
        val project = ExamProject(
            title = title,
            subject = subject,
            grade = grade,
            semester = semester,
            schoolYear = schoolYear,
            durationMinutes = durationMinutes,
            totalPoints = totalPoints,
            bookSeries = bookSeries,
            scopeDescription = scopeDescription,
            status = "Đang xây dựng"
        )
        val id = dao.insertProject(project)

        // Seed tailored initial matrix according to subject
        val matrix = createInitialMatrixForSubject(id, subject, grade)
        dao.insertMatrixRows(matrix)

        // Auto-generate matching specification rows
        val specs = syncSpecificationsFromMatrix(id, matrix)
        dao.insertSpecRows(specs)

        // Seed initial questions bám sát ma trận
        val sampleQ = DefaultCurriculumData.getSampleQuestions(id)
        dao.insertQuestions(sampleQ)

        id
    }

    suspend fun updateProject(project: ExamProject) = withContext(Dispatchers.IO) {
        dao.updateProject(project.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteProject(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteProjectById(id)
        dao.deleteMatrixRows(id)
        dao.deleteSpecRows(id)
        dao.deleteQuestionsForProject(id)
    }

    suspend fun saveMatrixRow(row: MatrixRow) = withContext(Dispatchers.IO) {
        val list = dao.getMatrixRowsDirect(row.projectId).toMutableList()
        val index = list.indexOfFirst { it.id == row.id }
        if (index >= 0) {
            list[index] = row
        } else {
            list.add(row)
        }
        dao.deleteMatrixRows(row.projectId)
        dao.insertMatrixRows(list)

        // Auto sync specifications
        val updatedSpecs = syncSpecificationsFromMatrix(row.projectId, list)
        dao.deleteSpecRows(row.projectId)
        dao.insertSpecRows(updatedSpecs)
    }

    suspend fun lockMatrix(projectId: Long, isLocked: Boolean) = withContext(Dispatchers.IO) {
        val proj = dao.getProjectByIdDirect(projectId)
        if (proj != null) {
            dao.updateProject(proj.copy(isMatrixLocked = isLocked, updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun updateQuestion(question: Question) = withContext(Dispatchers.IO) {
        dao.updateQuestion(question)
    }

    suspend fun regenerateQuestion(question: Question, instruction: String = ""): Question = withContext(Dispatchers.IO) {
        val newQ = GeminiService.regenerateQuestionAI(question, instruction)
        dao.updateQuestion(newQ)
        newQ
    }

    suspend fun duplicateProject(sourceProjectId: Long): Long = withContext(Dispatchers.IO) {
        val sourceProj = dao.getProjectByIdDirect(sourceProjectId) ?: return@withContext 0L
        val newProj = sourceProj.copy(
            id = 0L,
            title = "${sourceProj.title} (Bản sao)",
            updatedAt = System.currentTimeMillis(),
            version = sourceProj.version + 1
        )
        val newId = dao.insertProject(newProj)

        val matrix = dao.getMatrixRowsDirect(sourceProjectId).map { it.copy(id = 0L, projectId = newId) }
        dao.insertMatrixRows(matrix)

        val specs = dao.getSpecRowsDirect(sourceProjectId).map { it.copy(id = 0L, projectId = newId) }
        dao.insertSpecRows(specs)

        val questions = dao.getQuestionsDirect(sourceProjectId).map { it.copy(id = 0L, projectId = newId) }
        dao.insertQuestions(questions)

        newId
    }

    suspend fun generateExamVariants(
        projectId: Long,
        variantCount: Int
    ): List<ExamVariant> = withContext(Dispatchers.IO) {
        val questions = dao.getQuestionsDirect(projectId)
        val proj = dao.getProjectByIdDirect(projectId)
        val baseTitle = proj?.title ?: "Đề kiểm tra THCS Long Xuyên"

        val variants = mutableListOf<ExamVariant>()
        val startCode = 101

        for (i in 0 until variantCount) {
            val examCode = (startCode + i).toString()

            // Shuffle questions while preserving order of essay vs multiple choice if appropriate
            val mcqList = questions.filter { it.questionType != QuestionType.ESSAY }.shuffled()
            val essayList = questions.filter { it.questionType == QuestionType.ESSAY }

            val combined = (mcqList + essayList).mapIndexed { idx, q ->
                val newCode = "Câu " + (idx + 1)
                // Shuffle options for MCQ 4 options
                if (q.questionType == QuestionType.MULTIPLE_CHOICE_4) {
                    val rawOpts = q.getOptionsList().map { it.substringAfter(". ").trim() }
                    val shuffledOpts = rawOpts.shuffled()
                    val labels = listOf("A", "B", "C", "D")
                    val newOpts = shuffledOpts.mapIndexed { oIdx, text -> "${labels.getOrElse(oIdx) { "A" }}. $text" }

                    // Determine which label is now correct
                    val oldAnswerIndex = when (q.correctAnswer.trim().uppercase()) {
                        "A" -> 0
                        "B" -> 1
                        "C" -> 2
                        "D" -> 3
                        else -> 0
                    }
                    val originalCorrectText = rawOpts.getOrElse(oldAnswerIndex) { "" }
                    val newCorrectIndex = shuffledOpts.indexOf(originalCorrectText).coerceAtLeast(0)
                    val newCorrectLabel = labels.getOrElse(newCorrectIndex) { "A" }

                    q.copy(
                        questionCode = newCode,
                        optionsJson = JSONArray(newOpts).toString(),
                        correctAnswer = newCorrectLabel
                    )
                } else {
                    q.copy(questionCode = newCode)
                }
            }

            val answerKey = mutableMapOf<String, String>()
            val dist = mutableMapOf<CognitiveLevel, Int>()
            combined.forEach { q ->
                answerKey[q.questionCode] = q.correctAnswer
                dist[q.cognitiveLevel] = (dist[q.cognitiveLevel] ?: 0) + 1
            }

            variants.add(
                ExamVariant(
                    examCode = examCode,
                    title = "$baseTitle – Mã đề $examCode",
                    questions = combined,
                    answerKey = answerKey,
                    cognitiveDistribution = dist
                )
            )
        }
        variants
    }

    fun auditExam(
        matrix: List<MatrixRow>,
        specs: List<SpecificationRow>,
        questions: List<Question>
    ): List<ValidationAuditItem> {
        return GeminiService.auditExamSync(matrix, specs, questions)
    }

    private fun createInitialMatrixForSubject(
        projectId: Long,
        subject: SubjectType,
        grade: GradeLevel
    ): List<MatrixRow> {
        return when (subject) {
            SubjectType.TOAN -> listOf(
                MatrixRow(
                    projectId = projectId,
                    topic = "Đại số & Phân thức",
                    lesson = "Đa thức và Phân thức đại số",
                    recognitionCount = 4,
                    comprehensionCount = 2,
                    applicationCount = 2,
                    highApplicationCount = 0,
                    preferredType = QuestionType.MULTIPLE_CHOICE_4,
                    totalQuestions = 8,
                    points = 4.0,
                    percentage = 40.0
                ),
                MatrixRow(
                    projectId = projectId,
                    topic = "Hình học trực quan & phẳng",
                    lesson = "Định lý Pythagore & Tam giác đồng dạng",
                    recognitionCount = 2,
                    comprehensionCount = 2,
                    applicationCount = 1,
                    highApplicationCount = 1,
                    preferredType = QuestionType.SHORT_ANSWER,
                    totalQuestions = 6,
                    points = 4.0,
                    percentage = 40.0
                ),
                MatrixRow(
                    projectId = projectId,
                    topic = "Thống kê & Xác suất",
                    lesson = "Thu thập và phân tích dữ liệu",
                    recognitionCount = 2,
                    comprehensionCount = 1,
                    applicationCount = 1,
                    highApplicationCount = 0,
                    preferredType = QuestionType.ESSAY,
                    totalQuestions = 4,
                    points = 2.0,
                    percentage = 20.0
                )
            )
            SubjectType.NGU_VAN -> listOf(
                MatrixRow(
                    projectId = projectId,
                    topic = "Đọc hiểu văn bản",
                    lesson = "Truyện ngắn hiện đại & Thơ trữ tình",
                    recognitionCount = 4,
                    comprehensionCount = 4,
                    applicationCount = 0,
                    highApplicationCount = 0,
                    preferredType = QuestionType.MULTIPLE_CHOICE_4,
                    totalQuestions = 8,
                    points = 4.0,
                    percentage = 40.0
                ),
                MatrixRow(
                    projectId = projectId,
                    topic = "Thực hành Tiếng Việt",
                    lesson = "Từ ngữ toàn dân & Nghĩa của từ",
                    recognitionCount = 2,
                    comprehensionCount = 2,
                    applicationCount = 1,
                    highApplicationCount = 0,
                    preferredType = QuestionType.SHORT_ANSWER,
                    totalQuestions = 5,
                    points = 2.0,
                    percentage = 20.0
                ),
                MatrixRow(
                    projectId = projectId,
                    topic = "Viết đoạn văn",
                    lesson = "Nghị luận xã hội & Phân tích nhân vật",
                    recognitionCount = 0,
                    comprehensionCount = 0,
                    applicationCount = 1,
                    highApplicationCount = 1,
                    preferredType = QuestionType.ESSAY,
                    totalQuestions = 2,
                    points = 4.0,
                    percentage = 40.0
                )
            )
            else -> DefaultCurriculumData.getSampleMatrix(projectId)
        }
    }

    private fun syncSpecificationsFromMatrix(
        projectId: Long,
        matrix: List<MatrixRow>
    ): List<SpecificationRow> {
        val specs = mutableListOf<SpecificationRow>()
        var counter = 1

        matrix.forEach { row ->
            if (row.recognitionCount > 0) {
                specs.add(
                    SpecificationRow(
                        projectId = projectId,
                        topic = row.topic,
                        lesson = row.lesson,
                        standardRequirement = "Nhận biết được các khái niệm, định nghĩa và tính chất cơ bản của ${row.topic}.",
                        cognitiveLevel = CognitiveLevel.RECOGNITION,
                        questionType = QuestionType.MULTIPLE_CHOICE_4,
                        questionCount = row.recognitionCount,
                        points = row.recognitionCount * 0.25,
                        questionCode = "C${String.format("%02d", counter)} - C${String.format("%02d", counter + row.recognitionCount - 1)}"
                    )
                )
                counter += row.recognitionCount
            }

            if (row.comprehensionCount > 0) {
                specs.add(
                    SpecificationRow(
                        projectId = projectId,
                        topic = row.topic,
                        lesson = row.lesson,
                        standardRequirement = "Hiểu rõ bản chất, giải thích hiện tượng và phân biệt các trường hợp của ${row.lesson}.",
                        cognitiveLevel = CognitiveLevel.COMPREHENSION,
                        questionType = row.preferredType,
                        questionCount = row.comprehensionCount,
                        points = row.comprehensionCount * 0.5,
                        questionCode = "C${String.format("%02d", counter)} - C${String.format("%02d", counter + row.comprehensionCount - 1)}"
                    )
                )
                counter += row.comprehensionCount
            }

            if (row.applicationCount > 0) {
                specs.add(
                    SpecificationRow(
                        projectId = projectId,
                        topic = row.topic,
                        lesson = row.lesson,
                        standardRequirement = "Vận dụng công thức và quy tắc để giải quyết bài toán và tình huống thực tiễn ${row.topic}.",
                        cognitiveLevel = CognitiveLevel.APPLICATION,
                        questionType = if (row.preferredType == QuestionType.ESSAY) QuestionType.ESSAY else QuestionType.SHORT_ANSWER,
                        questionCount = row.applicationCount,
                        points = row.applicationCount * 0.75,
                        questionCode = "C${String.format("%02d", counter)} - C${String.format("%02d", counter + row.applicationCount - 1)}"
                    )
                )
                counter += row.applicationCount
            }

            if (row.highApplicationCount > 0) {
                specs.add(
                    SpecificationRow(
                        projectId = projectId,
                        topic = row.topic,
                        lesson = row.lesson,
                        standardRequirement = "Vận dụng sáng tạo, phân tích tổng hợp để giải bài toán phức hợp thuộc ${row.topic}.",
                        cognitiveLevel = CognitiveLevel.HIGH_APPLICATION,
                        questionType = QuestionType.ESSAY,
                        questionCount = row.highApplicationCount,
                        points = row.highApplicationCount * 1.0,
                        questionCode = "C${String.format("%02d", counter)}"
                    )
                )
                counter += row.highApplicationCount
            }
        }
        return specs
    }
}
