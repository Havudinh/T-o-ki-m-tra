package com.example.data.local

import com.example.data.model.CognitiveLevel
import com.example.data.model.ExamProject
import com.example.data.model.GradeLevel
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.data.model.SpecificationRow
import com.example.data.model.SubjectType
import org.json.JSONArray
import org.json.JSONObject

object DefaultCurriculumData {

    fun getDefaultProject(): ExamProject {
        return ExamProject(
            id = 1L,
            title = "Kiểm tra Giữa Học kỳ I – Môn Khoa học tự nhiên 8",
            subject = SubjectType.KHTN,
            grade = GradeLevel.GRADE_8,
            semester = "Học kỳ I",
            schoolYear = "2026–2027",
            durationMinutes = 60,
            totalPoints = 10.0,
            bookSeries = "Cánh Diều",
            scopeDescription = "Chủ đề 1: Phản ứng hóa học & Nồng độ dung dịch; Chủ đề 2: Lực và Chuyển động (Áp suất chất lỏng); Chủ đề 3: Máu và hệ tuần hoàn ở người",
            status = "Hoàn thành",
            isMatrixLocked = true,
            updatedAt = System.currentTimeMillis(),
            version = 1
        )
    }

    fun getSampleMatrix(projectId: Long): List<MatrixRow> {
        return listOf(
            MatrixRow(
                projectId = projectId,
                topic = "Phản ứng hóa học",
                lesson = "Định luật bảo toàn khối lượng & Phương trình hóa học",
                recognitionCount = 3,
                comprehensionCount = 2,
                applicationCount = 1,
                highApplicationCount = 0,
                preferredType = QuestionType.MULTIPLE_CHOICE_4,
                totalQuestions = 6,
                points = 2.5,
                percentage = 25.0
            ),
            MatrixRow(
                projectId = projectId,
                topic = "Dung dịch & Nồng độ",
                lesson = "Nồng độ phần trăm (C%) và Nồng độ mol (CM)",
                recognitionCount = 2,
                comprehensionCount = 2,
                applicationCount = 2,
                highApplicationCount = 0,
                preferredType = QuestionType.SHORT_ANSWER,
                totalQuestions = 6,
                points = 2.5,
                percentage = 25.0
            ),
            MatrixRow(
                projectId = projectId,
                topic = "Lực và Áp suất",
                lesson = "Áp suất chất lỏng, bình thông nhau & Áp suất khí quyển",
                recognitionCount = 3,
                comprehensionCount = 2,
                applicationCount = 1,
                highApplicationCount = 1,
                preferredType = QuestionType.TRUE_FALSE_4,
                totalQuestions = 7,
                points = 2.5,
                percentage = 25.0
            ),
            MatrixRow(
                projectId = projectId,
                topic = "Sinh học cơ thể người",
                lesson = "Máu, hệ tuần hoàn và các biện pháp bảo vệ tim mạch",
                recognitionCount = 2,
                comprehensionCount = 2,
                applicationCount = 1,
                highApplicationCount = 0,
                preferredType = QuestionType.ESSAY,
                totalQuestions = 5,
                points = 2.5,
                percentage = 25.0
            )
        )
    }

    fun getSampleSpecifications(projectId: Long): List<SpecificationRow> {
        return listOf(
            SpecificationRow(
                projectId = projectId,
                topic = "Phản ứng hóa học",
                lesson = "Định luật bảo toàn khối lượng",
                standardRequirement = "Phát biểu được định luật bảo toàn khối lượng. Nêu được dấu hiệu nhận biết phản ứng hóa học xảy ra.",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                questionCount = 3,
                points = 0.75,
                questionCode = "C01 - C03"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Phản ứng hóa học",
                lesson = "Phương trình hóa học",
                standardRequirement = "Lập được phương trình hóa học đơn giản. Giải thích được sự thay đổi liên kết giữa các nguyên tử.",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                questionCount = 2,
                points = 0.75,
                questionCode = "C04 - C05"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Phản ứng hóa học",
                lesson = "Tính toán theo định luật bảo toàn khối lượng",
                standardRequirement = "Áp dụng định luật tính được khối lượng của một chất khi biết khối lượng các chất còn lại trong phản ứng.",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                questionCount = 1,
                points = 1.0,
                questionCode = "C06"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Dung dịch & Nồng độ",
                lesson = "Khái niệm dung dịch & độ tan",
                standardRequirement = "Nêu được định nghĩa dung môi, chất tan, dung dịch bão hòa và chưa bão hòa.",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                questionCount = 2,
                points = 0.5,
                questionCode = "C07 - C08"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Dung dịch & Nồng độ",
                lesson = "Công thức nồng độ phần trăm và nồng độ mol",
                standardRequirement = "Viết được công thức tính C% và CM; giải thích ý nghĩa các đại lượng trong công thức.",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                questionCount = 2,
                points = 0.75,
                questionCode = "C09 - C10"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Dung dịch & Nồng độ",
                lesson = "Tính toán pha chế nồng độ dung dịch",
                standardRequirement = "Tính được khối lượng chất tan hoặc thể tích dung môi cần lấy để pha chế dung dịch có nồng độ theo yêu cầu.",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.SHORT_ANSWER,
                questionCount = 2,
                points = 1.25,
                questionCode = "C11 - C12"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Lực và Áp suất",
                lesson = "Áp suất chất lỏng & Khí quyển",
                standardRequirement = "Đánh giá tính đúng/sai của 4 phát biểu liên quan đến nguyên lý bình thông nhau, áp suất đáy bình và độ sâu của chất lỏng.",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.TRUE_FALSE_4,
                questionCount = 4,
                points = 1.5,
                questionCode = "C13 (Ý a, b, c, d)"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Lực và Áp suất",
                lesson = "Ứng dụng máy nén thủy lực & kích thủy lực",
                standardRequirement = "Vận dụng công thức máy nén thủy lực F2/F1 = S2/S1 để giải quyết tình huống nâng vật nặng trong thực tế đời sống.",
                cognitiveLevel = CognitiveLevel.HIGH_APPLICATION,
                questionType = QuestionType.SHORT_ANSWER,
                questionCount = 1,
                points = 1.0,
                questionCode = "C14"
            ),
            SpecificationRow(
                projectId = projectId,
                topic = "Sinh học cơ thể người",
                lesson = "Máu và hệ tuần hoàn",
                standardRequirement = "Trình bày cấu tạo các thành phần của máu; phân tích nguyên lý truyền máu an toàn và đề xuất 2 biện pháp giữ gìn hệ tim mạch khỏe mạnh.",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.ESSAY,
                questionCount = 1,
                points = 1.5,
                questionCode = "C15 (Tự luận)"
            )
        )
    }

    fun getSampleQuestions(projectId: Long): List<Question> {
        return listOf(
            Question(
                projectId = projectId,
                questionCode = "C01",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Phản ứng hóa học",
                lesson = "Định luật bảo toàn khối lượng",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Trong một phản ứng hóa học, đại lượng nào sau đây được bảo toàn trước và sau phản ứng?",
                optionsJson = JSONArray(listOf(
                    "A. Tổng khối lượng các chất",
                    "B. Số lượng phân tử của các chất",
                    "C. Thể tích của các chất",
                    "D. Trạng thái vật lý của các chất"
                )).toString(),
                correctAnswer = "A",
                explanation = "Theo định luật bảo toàn khối lượng của Lomonosov và Lavoisier: Trong một phản ứng hóa học, tổng khối lượng của các chất sản phẩm bằng tổng khối lượng của các chất tham gia phản ứng.",
                pedagogicalReason = "Câu hỏi thuộc mức Nhận biết vì học sinh chỉ cần nhớ lại nội dung cốt lõi của định luật bảo toàn khối lượng trong SGK KHTN 8 (Cánh Diều).",
                points = 0.25,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 2: Định luật bảo toàn khối lượng"
            ),
            Question(
                projectId = projectId,
                questionCode = "C02",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Phản ứng hóa học",
                lesson = "Dấu hiệu phản ứng hóa học",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Hiện tượng nào sau đây là dấu hiệu rõ ràng cho thấy có phản ứng hóa học xảy ra?",
                optionsJson = JSONArray(listOf(
                    "A. Nước đá tan chảy thành nước lỏng khi để ở nhiệt độ phòng",
                    "B. Xuất hiện chất khí thoát ra sủi bọt hoặc có chất kết tủa mới tạo thành",
                    "C. Cắt nhỏ một sợi dây đồng thành nhiều đoạn ngắn",
                    "D. Hòa tan muối ăn vào nước tạo thành dung dịch muối trong suốt"
                )).toString(),
                correctAnswer = "B",
                explanation = "Các dấu hiệu có phản ứng hóa học xảy ra gồm: có sự thay đổi màu sắc, xuất hiện chất khí bay lên, tạo chất kết tủa không tan, hoặc tỏa nhiệt phát sáng.",
                pedagogicalReason = "Thuộc mức Nhận biết, kiểm tra khả năng phân biệt hiện tượng hóa học và hiện tượng vật lý thông thường.",
                points = 0.25,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Chương 1: Phản ứng hóa học"
            ),
            Question(
                projectId = projectId,
                questionCode = "C03",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Phản ứng hóa học",
                lesson = "Phương trình hóa học",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Phương trình hóa học cho biết điều gì về phản ứng hóa học?",
                optionsJson = JSONArray(listOf(
                    "A. Tỉ lệ về số nguyên tử, số phân tử giữa các chất trong phản ứng",
                    "B. Thời gian chính xác để phản ứng kết thúc",
                    "C. Giá tiền của các hóa chất tham gia phản ứng",
                    "D. Thể tích bình phản ứng cần chuẩn bị"
                )).toString(),
                correctAnswer = "A",
                explanation = "Phương trình hóa học biểu diễn ngắn gọn phản ứng hóa học và cho biết tỉ lệ số nguyên tử, số phân tử giữa các chất trong phản ứng.",
                pedagogicalReason = "Mức Nhận biết - Yêu cầu cần đạt: Nêu được ý nghĩa của phương trình hóa học.",
                points = 0.25,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 3: Phương trình hóa học"
            ),
            Question(
                projectId = projectId,
                questionCode = "C04",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Phản ứng hóa học",
                lesson = "Cân bằng phương trình hóa học",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Cho sơ đồ phản ứng: Fe + O₂ → Fe₃O₄. Sau khi cân bằng với các hệ số nguyên tối giản, tổng hệ số của tất cả các chất trong phương trình là bao nhiêu?",
                optionsJson = JSONArray(listOf(
                    "A. 5",
                    "B. 6",
                    "C. 7",
                    "D. 8"
                )).toString(),
                correctAnswer = "B",
                explanation = "Phương trình cân bằng: 3Fe + 2O₂ → Fe₃O₄. Tổng hệ số các chất là: 3 + 2 + 1 = 6.",
                pedagogicalReason = "Mức Thông hiểu - Học sinh phải hiểu nguyên tắc bảo toàn số lượng nguyên tử mỗi nguyên tố để xác định hệ số cân bằng và tính tổng.",
                points = 0.5,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 3: Phương trình hóa học"
            ),
            Question(
                projectId = projectId,
                questionCode = "C05",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Phản ứng hóa học",
                lesson = "Bảo toàn nguyên tử",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Vì sao trong một phản ứng hóa học, khối lượng được bảo toàn nhưng thể tích chất khí có thể tăng hoặc giảm?",
                optionsJson = JSONArray(listOf(
                    "A. Vì liên kết giữa các nguyên tử bị thay đổi làm thay đổi số lượng phân tử khí",
                    "B. Vì nguyên tử bị phá hủy trong quá trình va chạm",
                    "C. Vì nhiệt độ làm các hạt electron biến mất",
                    "D. Vì áp suất khí quyển làm thay đổi khối lượng của nguyên tử"
                )).toString(),
                correctAnswer = "A",
                explanation = "Trong phản ứng hóa học, chỉ có liên kết giữa các nguyên tử thay đổi khiến phân tử này biến đổi thành phân tử khác (số lượng phân tử khí có thể thay đổi), còn bản thân các nguyên tử được giữ nguyên nên khối lượng luôn bảo toàn.",
                pedagogicalReason = "Mức Thông hiểu - Yêu cầu giải thích bản chất phân tử và nguyên tử trong phản ứng hóa học.",
                points = 0.5,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 2: Phản ứng hóa học"
            ),
            Question(
                projectId = projectId,
                questionCode = "C06",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Phản ứng hóa học",
                lesson = "Tính toán theo định luật bảo toàn khối lượng",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Đốt cháy hoàn toàn 6 gam carbon (C) trong khí oxygen (O₂) thu được 22 gam khí carbon dioxide (CO₂). Khối lượng khí oxygen đã tham gia phản ứng là:",
                optionsJson = JSONArray(listOf(
                    "A. 16 gam",
                    "B. 28 gam",
                    "C. 14 gam",
                    "D. 32 gam"
                )).toString(),
                correctAnswer = "A",
                explanation = "Theo định luật bảo toàn khối lượng: m(C) + m(O₂) = m(CO₂) => m(O₂) = m(CO₂) - m(C) = 22 - 6 = 16 gam.",
                pedagogicalReason = "Mức Vận dụng - Áp dụng trực tiếp công thức định luật bảo toàn khối lượng m(tham gia) = m(sản phẩm) để tìm khối lượng chất chưa biết.",
                points = 1.0,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài tập định luật bảo toàn khối lượng"
            ),
            Question(
                projectId = projectId,
                questionCode = "C07",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Dung dịch & Nồng độ",
                lesson = "Khái niệm dung dịch",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Dung dịch là gì?",
                optionsJson = JSONArray(listOf(
                    "A. Hỗn hợp đồng nhất của chất tan và dung môi",
                    "B. Hỗn hợp gồm chất rắn không tan lơ lửng trong nước",
                    "C. Hỗn hợp gồm các giọt chất lỏng lơ lửng trong chất lỏng khác",
                    "D. Hỗn hợp chất khí trộn lẫn với hạt bụi mịn"
                )).toString(),
                correctAnswer = "A",
                explanation = "Dung dịch là hỗn hợp đồng nhất giữa chất tan và dung môi.",
                pedagogicalReason = "Mức Nhận biết - Định nghĩa cơ bản về dung dịch trong KHTN 8.",
                points = 0.25,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 5: Dung dịch và độ tan"
            ),
            Question(
                projectId = projectId,
                questionCode = "C08",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Dung dịch & Nồng độ",
                lesson = "Dung môi thông dụng",
                cognitiveLevel = CognitiveLevel.RECOGNITION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Trong đời sống và trong phòng thí nghiệm, dung môi phổ biến và thông dụng nhất là:",
                optionsJson = JSONArray(listOf(
                    "A. Nước (H₂O)",
                    "B. Xăng dầu",
                    "C. Cồn rượu (Ethanol)",
                    "D. Dầu hỏa"
                )).toString(),
                correctAnswer = "A",
                explanation = "Nước là dung môi phân cực phổ biến nhất, có khả năng hòa tan rất nhiều muối, acid, base và các chất hữu cơ khác.",
                pedagogicalReason = "Mức Nhận biết - Kiến thức thực tiễn về dung môi.",
                points = 0.25,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 5: Dung dịch"
            ),
            Question(
                projectId = projectId,
                questionCode = "C09",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Dung dịch & Nồng độ",
                lesson = "Nồng độ phần trăm C%",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Công thức nào sau đây dùng để tính nồng độ phần trăm (C%) của dung dịch?",
                optionsJson = JSONArray(listOf(
                    "A. C% = (m_ct / m_dd) × 100%",
                    "B. C% = (m_dd / m_ct) × 100%",
                    "C. C% = (n / V) × 100%",
                    "D. C% = (m_ct × V) / 100%"
                )).toString(),
                correctAnswer = "A",
                explanation = "Nồng độ phần trăm (C%) là số gam chất tan có trong 100 gam dung dịch: C% = (m_ct / m_dd) × 100%.",
                pedagogicalReason = "Mức Thông hiểu - Nắm vững và phân biệt công thức nồng độ phần trăm.",
                points = 0.25,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 6: Nồng độ dung dịch"
            ),
            Question(
                projectId = projectId,
                questionCode = "C10",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Dung dịch & Nồng độ",
                lesson = "Nồng độ mol CM",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.MULTIPLE_CHOICE_4,
                content = "Đơn vị đo của nồng độ mol (CM) là:",
                optionsJson = JSONArray(listOf(
                    "A. mol/L (hoặc M)",
                    "B. gam/L",
                    "C. %",
                    "D. mol/gam"
                )).toString(),
                correctAnswer = "A",
                explanation = "Nồng độ mol (CM = n/V) cho biết số mol chất tan có trong 1 lít dung dịch, đơn vị là mol/L hoặc kí hiệu là M.",
                pedagogicalReason = "Mức Thông hiểu - Hiểu ý nghĩa vật lý và đơn vị của nồng độ mol.",
                points = 0.5,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài 6: Nồng độ dung dịch"
            ),
            Question(
                projectId = projectId,
                questionCode = "C11",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Dung dịch & Nồng độ",
                lesson = "Tính nồng độ phần trăm",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.SHORT_ANSWER,
                content = "Hòa tan hoàn toàn 20 gam đường vào 80 gam nước cất. Tính nồng độ phần trăm (C%) của dung dịch đường thu được? (Chỉ điền số kèm dấu %, ví dụ: 25%)",
                optionsJson = "[]",
                correctAnswer = "20%",
                explanation = "Khối lượng dung dịch: m_dd = m_ct + m_dm = 20 + 80 = 100 gam. Nồng độ C% = (20 / 100) × 100% = 20%.",
                pedagogicalReason = "Mức Vận dụng - Dạng câu hỏi trả lời ngắn: Yêu cầu tính khối lượng dung dịch rồi tính nồng độ phần trăm.",
                points = 0.5,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài tập nồng độ phần trăm"
            ),
            Question(
                projectId = projectId,
                questionCode = "C12",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Dung dịch & Nồng độ",
                lesson = "Tính nồng độ mol",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.SHORT_ANSWER,
                content = "Hòa tan 0,5 mol NaCl vào nước thu được 250 mL dung dịch. Tính nồng độ mol của dung dịch NaCl thu được? (Điền số kèm đơn vị M, ví dụ: 1.5M hoặc 2M)",
                optionsJson = "[]",
                correctAnswer = "2M",
                explanation = "Đổi V = 250 mL = 0,25 L. Áp dụng công thức CM = n / V = 0,5 / 0,25 = 2 M.",
                pedagogicalReason = "Mức Vận dụng - Yêu cầu đổi đơn vị thể tích sang lít và áp dụng công thức nồng độ mol.",
                points = 0.75,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Bài tập nồng độ mol"
            ),
            Question(
                projectId = projectId,
                questionCode = "C13",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Lực và Áp suất",
                lesson = "Áp suất chất lỏng & Khí quyển",
                cognitiveLevel = CognitiveLevel.COMPREHENSION,
                questionType = QuestionType.TRUE_FALSE_4,
                content = "Xét các nhận định sau đây về áp suất chất lỏng và bình thông nhau:\na) Chất lỏng gây ra áp suất theo mọi phương lên đáy bình, thành bình và các vật ở trong lòng nó.\nb) Càng xuống sâu trong lòng chất lỏng thì áp suất do chất lỏng gây ra càng giảm.\nc) Trong bình thông nhau chứa cùng một chất lỏng đứng yên, mực chất lỏng ở các nhánh luôn ở cùng độ cao.\nd) Áp suất khí quyển tác dụng theo mọi phương lên mọi vật trên Trái Đất.",
                optionsJson = JSONArray(listOf(
                    "Ý a: Chất lỏng gây áp suất theo mọi phương",
                    "Ý b: Càng xuống sâu áp suất càng giảm",
                    "Ý c: Mực chất lỏng bình thông nhau ở cùng độ cao",
                    "Ý d: Áp suất khí quyển tác dụng theo mọi phương"
                )).toString(),
                correctAnswer = "a) Đúng; b) Sai; c) Đúng; d) Đúng",
                explanation = "Ý b sai vì theo công thức p = d × h, càng xuống sâu (độ sâu h tăng) thì áp suất chất lỏng càng tăng chứ không giảm.",
                pedagogicalReason = "Dạng câu hỏi Đúng/Sai (4 ý) chuẩn format Bộ GD&ĐT mới. Kiểm tra toàn diện sự hiểu biết về áp suất chất lỏng và khí quyển.",
                points = 1.5,
                rubricJson = JSONArray(listOf(
                    JSONObject().put("criteria", "Trả lời đúng 1 ý: 0.25đ; đúng 2 ý: 0.5đ; đúng 3 ý: 1.0đ; đúng cả 4 ý: 1.5đ").put("points", 1.5)
                )).toString(),
                knowledgeSource = "SGK KHTN 8 - Bài 16: Áp suất chất lỏng. Áp suất khí quyển"
            ),
            Question(
                projectId = projectId,
                questionCode = "C14",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Lực và Áp suất",
                lesson = "Máy nén thủy lực",
                cognitiveLevel = CognitiveLevel.HIGH_APPLICATION,
                questionType = QuestionType.SHORT_ANSWER,
                content = "Một máy nén thủy lực có tiết diện pít-tông nhỏ là 2 cm² và pít-tông lớn là 100 cm². Muốn nâng một chiếc ô tô có khối lượng 1,5 tấn (tương đương trọng lượng 15 000 N) thì cần tác dụng một lực F₁ tối thiểu vào pít-tông nhỏ bằng bao nhiêu Newton (N)? (Điền số kèm đơn vị N, ví dụ: 300N)",
                optionsJson = "[]",
                correctAnswer = "300N",
                explanation = "Theo nguyên lý máy nén thủy lực: F₂ / F₁ = S₂ / S₁ <=> 15 000 / F₁ = 100 / 2 = 50 => F₁ = 15 000 / 50 = 300 N.",
                pedagogicalReason = "Mức Vận dụng cao - Học sinh vận dụng kiến thức truyền áp suất chất lỏng theo mọi phương (Định luật Pascal) để giải bài toán cơ học kỹ thuật.",
                points = 1.0,
                rubricJson = "[]",
                knowledgeSource = "SGK KHTN 8 - Ứng dụng áp suất chất lỏng"
            ),
            Question(
                projectId = projectId,
                questionCode = "C15",
                subject = SubjectType.KHTN,
                grade = GradeLevel.GRADE_8,
                topic = "Sinh học cơ thể người",
                lesson = "Máu và hệ tuần hoàn",
                cognitiveLevel = CognitiveLevel.APPLICATION,
                questionType = QuestionType.ESSAY,
                content = "a) Hãy nêu các thành phần cấu tạo chính của máu ở người và chức năng của từng thành phần.\nb) Để bảo vệ và rèn luyện hệ tim mạch khỏe mạnh, em hãy đề xuất ít nhất 3 thói quen sinh hoạt khoa học, lành mạnh mà học sinh THCS nên thực hiện hằng ngày.",
                optionsJson = "[]",
                correctAnswer = "Hướng dẫn chấm chi tiết: Có 2 phần a và b.",
                explanation = "Thành phần máu gồm huyết tương và các tế bào máu (hồng cầu, bạch cầu, tiểu cầu). Thói quen bảo vệ tim mạch: tập thể dục đều đặn, không ăn quá nhiều muối/dầu mỡ, ngủ đủ giấc.",
                pedagogicalReason = "Mức Vận dụng - Tự luận có hướng dẫn chấm chi tiết (Rubric) theo từng tiêu chí, đánh giá năng lực liên hệ thực tiễn chăm sóc sức khỏe bản thân.",
                points = 1.5,
                rubricJson = JSONArray(listOf(
                    JSONObject().put("criteria", "Ý a1: Nêu đúng huyết tương (chiếm 55%) vận chuyển chất dinh dưỡng, chất thải, hoocmôn").put("points", 0.3),
                    JSONObject().put("criteria", "Ý a2: Nêu đúng các tế bào máu: Hồng cầu (vận chuyển O2, CO2), Bạch cầu (bảo vệ miễn dịch), Tiểu cầu (đông máu)").put("points", 0.5),
                    JSONObject().put("criteria", "Ý b1: Tập luyện thể dục thể thao điều độ, vừa sức (chạy bộ, đạp xe, bơi lội)").put("points", 0.25),
                    JSONObject().put("criteria", "Ý b2: Chế độ ăn uống cân bằng, hạn chế đồ chiên rán nhiều mỡ động vật, hạn chế ăn quá mặn").put("points", 0.25),
                    JSONObject().put("criteria", "Ý b3: Giữ tinh thần lạc quan, ngủ đủ giấc, tránh xa chất kích thích (thuốc lá, rượu bia, nước ngọt có ga)").put("points", 0.2)
                )).toString(),
                knowledgeSource = "SGK KHTN 8 - Chương 7: Hệ tuần hoàn ở người"
            )
        )
    }

    fun getSampleReferenceDocs(): List<Map<String, String>> {
        return listOf(
            mapOf(
                "fileName" to "Ma_tran_KHTN8_GiuaKy1_BGD2026.docx",
                "fileType" to "DOCX",
                "size" to "142 KB",
                "status" to "Đã phân tích",
                "summary" to "Ma trận chuẩn 4 mức độ nhận thức (NB 40%, TH 30%, VD 20%, VDC 10%) gồm 4 chủ đề môn KHTN 8."
            ),
            mapOf(
                "fileName" to "Bang_dac_ta_KHTN8_CanhDieu.xlsx",
                "fileType" to "XLSX",
                "size" to "88 KB",
                "status" to "Đã phân tích",
                "summary" to "Bảng đặc tả 15 đơn vị kiến thức, tích hợp đầy đủ yêu cầu cần đạt chương trình 2018."
            ),
            mapOf(
                "fileName" to "De_thi_mau_THCS_Long_Xuyen_2025.pdf",
                "fileType" to "PDF",
                "size" to "520 KB",
                "status" to "Đã phân tích",
                "summary" to "Đề thi mẫu phân phối tỉ lệ TN 70% (TN 4 lựa chọn, Đúng/Sai, Điền số) và Tự luận 30%."
            )
        )
    }
}
