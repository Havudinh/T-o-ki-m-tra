package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CognitiveLevel
import com.example.data.model.DocumentAnalysisReport
import com.example.data.model.ExamProject
import com.example.data.model.ExamVariant
import com.example.data.model.GradeLevel
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.data.model.SpecificationRow
import com.example.data.model.SubjectType
import com.example.data.model.ValidationAuditItem
import com.example.data.model.ValidationStatus
import com.example.data.remote.GeminiService
import com.example.data.repository.ExamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    WIZARD,
    QUESTION_BANK,
    MATRIX_SPEC,
    HISTORY,
    SETTINGS,
    EXAM_PREVIEW
}

data class ChatMessage(
    val sender: String, // "user" or "ai"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class UiNotification(
    val message: String,
    val isError: Boolean = false
)

class ExamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ExamRepository(application)

    val allProjects: StateFlow<List<ExamProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBankQuestions: StateFlow<List<Question>> = repository.allBankQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _activeProjectId = MutableStateFlow<Long>(1L)
    val activeProjectId: StateFlow<Long> = _activeProjectId.asStateFlow()

    private val _wizardStep = MutableStateFlow(1)
    val wizardStep: StateFlow<Int> = _wizardStep.asStateFlow()

    // Current Project Data
    private val _currentProject = MutableStateFlow<ExamProject?>(null)
    val currentProject: StateFlow<ExamProject?> = _currentProject.asStateFlow()

    private val _matrixRows = MutableStateFlow<List<MatrixRow>>(emptyList())
    val matrixRows: StateFlow<List<MatrixRow>> = _matrixRows.asStateFlow()

    private val _specRows = MutableStateFlow<List<SpecificationRow>>(emptyList())
    val specRows: StateFlow<List<SpecificationRow>> = _specRows.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    // Wizard step 3: AI Document Analysis
    private val _analysisReport = MutableStateFlow(DocumentAnalysisReport())
    val analysisReport: StateFlow<DocumentAnalysisReport> = _analysisReport.asStateFlow()

    private val _isAnalyzingDocs = MutableStateFlow(false)
    val isAnalyzingDocs: StateFlow<Boolean> = _isAnalyzingDocs.asStateFlow()

    // Step 8: Multi Exam Variants
    private val _examVariants = MutableStateFlow<List<ExamVariant>>(emptyList())
    val examVariants: StateFlow<List<ExamVariant>> = _examVariants.asStateFlow()

    private val _variantCount = MutableStateFlow(4)
    val variantCount: StateFlow<Int> = _variantCount.asStateFlow()

    // Step 9: Validation Audit Items
    private val _auditItems = MutableStateFlow<List<ValidationAuditItem>>(emptyList())
    val auditItems: StateFlow<List<ValidationAuditItem>> = _auditItems.asStateFlow()

    // AI Assistant Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "ai",
                content = "Xin chào thầy/cô! Em là Trợ lý xây dựng đề kiểm tra THCS Long Xuyên. Thầy/cô có thể yêu cầu: 'Tạo thêm 3 câu vận dụng về áp suất chất lỏng', 'Đổi câu 12 sang mức thông hiểu', hoặc 'Gợi ý ma trận chuẩn cho môn Toán 8'!"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _notification = MutableStateFlow<UiNotification?>(null)
    val notification: StateFlow<UiNotification?> = _notification.asStateFlow()

    // Question Bank Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilterSubject = MutableStateFlow<SubjectType?>(null)
    val selectedFilterSubject: StateFlow<SubjectType?> = _selectedFilterSubject.asStateFlow()

    private val _selectedFilterLevel = MutableStateFlow<CognitiveLevel?>(null)
    val selectedFilterLevel: StateFlow<CognitiveLevel?> = _selectedFilterLevel.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureDefaultDataLoaded()
            loadProject(1L)
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun goToHome() {
        _currentScreen.value = AppScreen.HOME
    }

    fun setWizardStep(step: Int) {
        _wizardStep.value = step.coerceIn(1, 10)
        if (step == 8 && _examVariants.value.isEmpty()) {
            generateVariants(_variantCount.value)
        }
        if (step == 9) {
            runValidationAudit()
        }
    }

    fun nextStep() {
        setWizardStep(_wizardStep.value + 1)
    }

    fun previousStep() {
        setWizardStep(_wizardStep.value - 1)
    }

    fun loadProject(projectId: Long) {
        _activeProjectId.value = projectId
        viewModelScope.launch {
            repository.getProject(projectId).collect { proj ->
                _currentProject.value = proj
            }
        }
        viewModelScope.launch {
            repository.getMatrixRows(projectId).collect { rows ->
                _matrixRows.value = rows
            }
        }
        viewModelScope.launch {
            repository.getSpecRows(projectId).collect { specs ->
                _specRows.value = specs
            }
        }
        viewModelScope.launch {
            repository.getQuestions(projectId).collect { qList ->
                _questions.value = qList
                runValidationAudit()
            }
        }
    }

    fun createNewProject(
        title: String,
        subject: SubjectType,
        grade: GradeLevel,
        semester: String,
        schoolYear: String,
        durationMinutes: Int,
        totalPoints: Double,
        bookSeries: String,
        scopeDescription: String
    ) {
        viewModelScope.launch {
            val newId = repository.createNewProject(
                title = title,
                subject = subject,
                grade = grade,
                semester = semester,
                schoolYear = schoolYear,
                durationMinutes = durationMinutes,
                totalPoints = totalPoints,
                bookSeries = bookSeries,
                scopeDescription = scopeDescription
            )
            loadProject(newId)
            _wizardStep.value = 1
            _currentScreen.value = AppScreen.WIZARD
            _notification.value = UiNotification("Đã khởi tạo dự án: $title")
        }
    }

    fun runDocumentAnalysis() {
        viewModelScope.launch {
            _isAnalyzingDocs.value = true
            kotlinx.coroutines.delay(1200) // Simulating deep OCR & structural table recognition
            _analysisReport.value = DocumentAnalysisReport(
                matrixCount = 2,
                specCount = 2,
                sampleExamCount = 4,
                referenceQuestionCount = 68,
                topicsDetected = listOf("Phản ứng hóa học", "Nồng độ dung dịch", "Lực và Áp suất", "Cơ thể người"),
                cognitiveLevelsSummary = "Nhận biết 40% (4.0đ) • Thông hiểu 30% (3.0đ) • Vận dụng 20% (2.0đ) • Vận dụng cao 10% (1.0đ)",
                questionTypesDetected = listOf("Trắc nghiệm 4 lựa chọn", "Đúng/Sai (4 ý)", "Trả lời ngắn", "Tự luận"),
                aiConsistencyStatus = ValidationStatus.VALID,
                summaryNotes = "Đã phân tích 4 tài liệu tham chiếu. Không phát hiện trùng lặp. Ma trận khớp 100% chuẩn GDPT 2018 THCS Long Xuyên."
            )
            _isAnalyzingDocs.value = false
            _notification.value = UiNotification("Đã hoàn tất phân tích tài liệu!")
        }
    }

    fun updateMatrixRow(row: MatrixRow) {
        viewModelScope.launch {
            repository.saveMatrixRow(row)
            _notification.value = UiNotification("Đã cập nhật ma trận & đồng bộ bảng đặc tả!")
        }
    }

    fun toggleMatrixLock() {
        val current = _currentProject.value ?: return
        val newLock = !current.isMatrixLocked
        viewModelScope.launch {
            repository.lockMatrix(current.id, newLock)
            _notification.value = UiNotification(if (newLock) "🔒 Đã khóa ma trận! AI không tự ý thay đổi." else "🔓 Đã mở khóa ma trận.")
        }
    }

    fun regenerateSingleQuestion(question: Question) {
        viewModelScope.launch {
            _isAiThinking.value = true
            val updated = repository.regenerateQuestion(question)
            _isAiThinking.value = false
            _notification.value = UiNotification("Đã sinh câu hỏi mới thay thế cho ${question.questionCode}!")
        }
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            repository.updateQuestion(question)
            _notification.value = UiNotification("Đã lưu chỉnh sửa câu hỏi ${question.questionCode}")
        }
    }

    fun generateVariants(count: Int) {
        _variantCount.value = count
        viewModelScope.launch {
            _isAiThinking.value = true
            val variants = repository.generateExamVariants(_activeProjectId.value, count)
            _examVariants.value = variants
            _isAiThinking.value = false
            _notification.value = UiNotification("Đã tạo thành công $count mã đề độc lập!")
        }
    }

    fun runValidationAudit() {
        val items = repository.auditExam(
            _matrixRows.value,
            _specRows.value,
            _questions.value
        )
        _auditItems.value = items
    }

    fun sendAiAssistantQuery(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage(sender = "user", content = prompt)
        _chatMessages.value = _chatMessages.value + userMsg

        val currentProj = _currentProject.value
        val subj = currentProj?.subject ?: SubjectType.KHTN
        val grade = currentProj?.grade ?: GradeLevel.GRADE_8
        val qCount = _questions.value.size

        viewModelScope.launch {
            _isAiThinking.value = true
            val reply = GeminiService.generateAssistantAdvice(prompt, subj, grade, qCount)
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = "ai", content = reply)
            _isAiThinking.value = false
        }
    }

    fun duplicateProject(projectId: Long) {
        viewModelScope.launch {
            val newId = repository.duplicateProject(projectId)
            if (newId > 0L) {
                loadProject(newId)
                _notification.value = UiNotification("Đã nhân bản dự án thành công!")
            }
        }
    }

    fun updateProject(project: ExamProject) {
        viewModelScope.launch {
            repository.updateProject(project)
            _currentProject.value = project
            _notification.value = UiNotification("Đã cập nhật dự án thành công!")
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            _notification.value = UiNotification("Đã xóa dự án")
            val remainingList = repository.allProjects.firstOrNull() ?: emptyList()
            if (remainingList.isNotEmpty()) {
                loadProject(remainingList.first().id)
            }
        }
    }

    fun clearNotification() {
        _notification.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterSubject(subj: SubjectType?) {
        _selectedFilterSubject.value = subj
    }

    fun setFilterLevel(level: CognitiveLevel?) {
        _selectedFilterLevel.value = level
    }
}
