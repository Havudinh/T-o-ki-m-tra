package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

enum class SubjectType(val displayName: String, val code: String) {
    KHTN("Khoa học tự nhiên", "KHTN"),
    TOAN("Toán", "TOAN"),
    NGU_VAN("Ngữ văn", "VAN"),
    TIENG_ANH("Tiếng Anh", "ANH"),
    LICH_SU_DIA_LI("Lịch sử và Địa lí", "LSDL"),
    TIN_HOC("Tin học", "TIN"),
    CONG_NGHE("Công nghệ", "CN"),
    GDCD("GDCD / GDKT&PL", "GDCD"),
    VAT_LI("Vật lí", "LY"),
    HOA_HOC("Hóa học", "HOA"),
    SINH_HOC("Sinh học", "SINH"),
    KHAC("Môn khác", "KHAC")
}

enum class GradeLevel(val displayName: String, val code: String) {
    GRADE_6("Lớp 6", "6"),
    GRADE_7("Lớp 7", "7"),
    GRADE_8("Lớp 8", "8"),
    GRADE_9("Lớp 9", "9"),
    GRADE_10("Lớp 10 (THPT)", "10"),
    GRADE_11("Lớp 11 (THPT)", "11"),
    GRADE_12("Lớp 12 (THPT)", "12")
}

enum class CognitiveLevel(val displayName: String, val shortName: String, val colorHex: Long) {
    RECOGNITION("Nhận biết", "NB", 0xFF1E88E5),
    COMPREHENSION("Thông hiểu", "TH", 0xFF43A047),
    APPLICATION("Vận dụng", "VD", 0xFFFB8C00),
    HIGH_APPLICATION("Vận dụng cao", "VDC", 0xFFE53935)
}

enum class QuestionType(val displayName: String, val code: String) {
    MULTIPLE_CHOICE_4("Trắc nghiệm (4 lựa chọn)", "TN4"),
    TRUE_FALSE_4("Đúng / Sai (4 ý)", "DS4"),
    SHORT_ANSWER("Trả lời ngắn", "TLN"),
    ESSAY("Tự luận", "TL")
}

enum class ValidationStatus(val displayName: String, val symbol: String) {
    VALID("Hợp lệ", "🟢"),
    NEEDS_CHECK("Cần kiểm tra", "🟡"),
    INVALID("Không hợp lệ", "🔴")
}

@Entity(tableName = "projects")
data class ExamProject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: SubjectType,
    val grade: GradeLevel,
    val semester: String = "Học kỳ I",
    val schoolYear: String = "2026–2027",
    val durationMinutes: Int = 45,
    val totalPoints: Double = 10.0,
    val bookSeries: String = "Cánh Diều", // Cánh Diều, Kết nối tri thức, Chân trời sáng tạo
    val scopeDescription: String = "",
    val status: String = "Đang chỉnh sửa",
    val isMatrixLocked: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val version: Int = 1
)

@Entity(tableName = "matrix_rows")
data class MatrixRow(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val topic: String, // Chủ đề
    val lesson: String, // Nội dung / Bài
    val recognitionCount: Int = 0, // Nhận biết
    val comprehensionCount: Int = 0, // Thông hiểu
    val applicationCount: Int = 0, // Vận dụng
    val highApplicationCount: Int = 0, // Vận dụng cao
    val preferredType: QuestionType = QuestionType.MULTIPLE_CHOICE_4,
    val totalQuestions: Int = 0,
    val points: Double = 0.0,
    val percentage: Double = 0.0
)

@Entity(tableName = "specification_rows")
data class SpecificationRow(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val topic: String,
    val lesson: String,
    val standardRequirement: String, // Yêu cầu cần đạt
    val cognitiveLevel: CognitiveLevel,
    val questionType: QuestionType,
    val questionCount: Int,
    val points: Double,
    val questionCode: String // e.g. "C01", "C02"
)

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val questionCode: String,
    val subject: SubjectType,
    val grade: GradeLevel,
    val topic: String,
    val lesson: String,
    val cognitiveLevel: CognitiveLevel,
    val questionType: QuestionType,
    val content: String,
    val optionsJson: String = "[]", // JSON array of options e.g. ["A. ...", "B. ..."]
    val correctAnswer: String, // e.g. "A" or "Ý 1: Đ, Ý 2: S..." or "42.5"
    val explanation: String, // Giải thích vì sao đáp án đúng
    val pedagogicalReason: String = "", // Chế độ "Vì sao?" (Mức độ, ma trận, yêu cầu cần đạt)
    val points: Double = 0.25,
    val rubricJson: String = "[]", // Hướng dẫn chấm theo từng ý
    val knowledgeSource: String = "SGK THCS Chuẩn GDPT 2018",
    val isApproved: Boolean = false,
    val isFavorite: Boolean = false,
    val formulaLatex: String = "" // Hỗ trợ công thức KHTN/Toán
) {
    fun getOptionsList(): List<String> {
        val list = mutableListOf<String>()
        try {
            val jsonArr = JSONArray(optionsJson)
            for (i in 0 until jsonArr.length()) {
                list.add(jsonArr.getString(i))
            }
        } catch (_: Exception) {}
        return list
    }

    fun getRubricList(): List<Pair<String, Double>> {
        val list = mutableListOf<Pair<String, Double>>()
        try {
            val jsonArr = JSONArray(rubricJson)
            for (i in 0 until jsonArr.length()) {
                val obj = jsonArr.getJSONObject(i)
                list.add(Pair(obj.optString("criteria"), obj.optDouble("points", 0.0)))
            }
        } catch (_: Exception) {}
        return list
    }
}

data class ExamVariant(
    val examCode: String, // e.g. "101", "102", "103", "104"
    val title: String,
    val questions: List<Question>,
    val answerKey: Map<String, String>, // QuestionCode -> Correct Option/Answer
    val cognitiveDistribution: Map<CognitiveLevel, Int>
)

data class DocumentAnalysisReport(
    val matrixCount: Int = 1,
    val specCount: Int = 1,
    val sampleExamCount: Int = 3,
    val referenceQuestionCount: Int = 48,
    val topicsDetected: List<String> = listOf(),
    val cognitiveLevelsSummary: String = "Nhận biết 40% - Thông hiểu 30% - Vận dụng 20% - Vận dụng cao 10%",
    val questionTypesDetected: List<String> = listOf("Trắc nghiệm 4 lựa chọn", "Đúng/Sai", "Trả lời ngắn", "Tự luận"),
    val aiConsistencyStatus: ValidationStatus = ValidationStatus.VALID,
    val summaryNotes: String = "Cấu trúc ma trận đạt chuẩn GDPT 2018. Phân bổ điểm 10.0 đồng đều, không phát hiện lỗi trùng lặp."
)

data class ValidationAuditItem(
    val criteria: String,
    val matrixTarget: String,
    val actualExam: String,
    val status: ValidationStatus,
    val notes: String = ""
)

data class FullValidationReport(
    val overallStatus: ValidationStatus,
    val items: List<ValidationAuditItem>,
    val canExport: Boolean
)
