package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DefaultCurriculumData
import com.example.data.model.CognitiveLevel
import com.example.data.model.GradeLevel
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.data.model.SpecificationRow
import com.example.data.model.SubjectType
import com.example.data.model.ValidationStatus
import com.example.ui.components.QuestionCard
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.ValidGreen
import com.example.ui.theme.WarningYellow
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ExamViewModel

@Composable
fun WizardScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.goToHome()
    }

    val step by viewModel.wizardStep.collectAsStateWithLifecycle()
    val project by viewModel.currentProject.collectAsStateWithLifecycle()
    val matrixRows by viewModel.matrixRows.collectAsStateWithLifecycle()
    val specRows by viewModel.specRows.collectAsStateWithLifecycle()
    val questions by viewModel.questions.collectAsStateWithLifecycle()
    val analysisReport by viewModel.analysisReport.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzingDocs.collectAsStateWithLifecycle()
    val variants by viewModel.examVariants.collectAsStateWithLifecycle()
    val auditItems by viewModel.auditItems.collectAsStateWithLifecycle()

    val stepNames = listOf(
        "1. Thông tin",
        "2. Tài liệu",
        "3. Phân tích AI",
        "4. Ma trận",
        "5. Bảng đặc tả",
        "6. Sinh câu hỏi",
        "7. Duyệt câu",
        "8. Nhiều mã đề",
        "9. Kiểm định",
        "10. Xuất file"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Step progress horizontal indicator
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            ScrollableTabRow(
                selectedTabIndex = step - 1,
                edgePadding = 8.dp,
                containerColor = Color.White,
                contentColor = SchoolNavy
            ) {
                stepNames.forEachIndexed { index, name ->
                    Tab(
                        selected = step == index + 1,
                        onClick = { viewModel.setWizardStep(index + 1) },
                        text = {
                            Text(
                                text = name,
                                fontWeight = if (step == index + 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }
        }

        // Active Step Content Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            when (step) {
                1 -> Step1Info(project = project, viewModel = viewModel)
                2 -> Step2ReferenceDocs(viewModel = viewModel)
                3 -> Step3AnalysisReport(report = analysisReport, isAnalyzing = isAnalyzing, viewModel = viewModel)
                4 -> Step4Matrix(matrixRows = matrixRows, project = project, viewModel = viewModel)
                5 -> Step5Specifications(specRows = specRows, viewModel = viewModel)
                6, 7 -> Step6And7Questions(questions = questions, viewModel = viewModel)
                8 -> Step8Variants(variants = variants, viewModel = viewModel)
                9 -> Step9Validation(auditItems = auditItems, viewModel = viewModel)
                10 -> Step10Export(project = project, variants = variants, questions = questions, viewModel = viewModel)
            }
        }

        // Bottom Wizard Navigation Bar (Previous & Next)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { viewModel.previousStep() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Quay lại")
                    }
                } else {
                    OutlinedButton(
                        onClick = { viewModel.goToHome() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Về Trang chủ")
                    }
                }

                Text(
                    text = "Bước $step / 10",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                if (step < 10) {
                    Button(
                        onClick = { viewModel.nextStep() },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Tiếp theo")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.EXAM_PREVIEW) },
                        colors = ButtonDefaults.buttonColors(containerColor = ValidGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Xem đề in")
                    }
                }
            }
        }
    }
}

// ----------------- STEP 1: INFO -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Info(project: com.example.data.model.ExamProject?, viewModel: ExamViewModel) {
    var title by remember(project) { mutableStateOf(project?.title ?: "Đề kiểm tra KHTN 8") }
    var selectedSubject by remember(project) { mutableStateOf(project?.subject ?: SubjectType.KHTN) }
    var selectedGrade by remember(project) { mutableStateOf(project?.grade ?: GradeLevel.GRADE_8) }
    var semester by remember(project) { mutableStateOf(project?.semester ?: "Học kỳ I") }
    var bookSeries by remember(project) { mutableStateOf(project?.bookSeries ?: "Cánh Diều") }
    var durationMinutes by remember(project) { mutableStateOf((project?.durationMinutes ?: 60).toString()) }
    var scope by remember(project) { mutableStateOf(project?.scopeDescription ?: "") }

    var subjectExpanded by remember { mutableStateOf(false) }
    var gradeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "BƯỚC 1: CHỌN MÔN – KHỐI – CHƯƠNG TRÌNH",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SchoolNavy
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tên bài kiểm tra") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ExposedDropdownMenuBox(
                expanded = subjectExpanded,
                onExpandedChange = { subjectExpanded = !subjectExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedSubject.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Môn học") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = subjectExpanded, onDismissRequest = { subjectExpanded = false }) {
                    SubjectType.values().forEach { subj ->
                        DropdownMenuItem(text = { Text(subj.displayName) }, onClick = {
                            selectedSubject = subj
                            subjectExpanded = false
                        })
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = gradeExpanded,
                onExpandedChange = { gradeExpanded = !gradeExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedGrade.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Khối lớp") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = gradeExpanded, onDismissRequest = { gradeExpanded = false }) {
                    GradeLevel.values().take(4).forEach { gr ->
                        DropdownMenuItem(text = { Text(gr.displayName) }, onClick = {
                            selectedGrade = gr
                            gradeExpanded = false
                        })
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = bookSeries,
                onValueChange = { bookSeries = it },
                label = { Text("Bộ sách giáo khoa") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = durationMinutes,
                onValueChange = { durationMinutes = it.filter { ch -> ch.isDigit() } },
                label = { Text("Thời lượng (phút)") },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = scope,
            onValueChange = { scope = it },
            label = { Text("Phạm vi kiến thức & Yêu cầu trọng tâm") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                if (project != null) {
                    viewModel.updateProject(
                        project.copy(
                            title = title,
                            subject = selectedSubject,
                            grade = selectedGrade,
                            bookSeries = bookSeries,
                            durationMinutes = durationMinutes.toIntOrNull() ?: 60,
                            scopeDescription = scope
                        )
                    )
                }
                viewModel.nextStep()
            },
            colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Xác nhận thông tin & Sang Bước 2")
        }
    }
}

// ----------------- STEP 2: REFERENCE DOCS -----------------
@Composable
fun Step2ReferenceDocs(viewModel: ExamViewModel) {
    val sampleDocs = remember { DefaultCurriculumData.getSampleReferenceDocs() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "BƯỚC 2: CUNG CẤP DỮ LIỆU THAM CHIẾU",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SchoolNavy
        )

        // Drop zone mockup
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEFF6FF))
                .border(1.5.dp, Color(0xFF93C5FD), RoundedCornerShape(12.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = SchoolNavy,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "KÉO THẢ HOẶC CHỌN TÀI LIỆU VÀO ĐÂY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = SchoolNavy
                )
                Text(
                    text = "Hỗ trợ DOCX, XLSX, PDF, PPTX, TXT, CSV, Hình ảnh scan",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Quick action upload buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.runDocumentAnalysis() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Tải ma trận mẫu", fontSize = 11.sp)
            }
            OutlinedButton(
                onClick = { viewModel.runDocumentAnalysis() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Tải đặc tả mẫu", fontSize = 11.sp)
            }
            OutlinedButton(
                onClick = { viewModel.runDocumentAnalysis() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Tải đề mẫu", fontSize = 11.sp)
            }
        }

        Text(
            text = "TÀI LIỆU THAM CHIẾU ĐÃ KẾT NỐI (${sampleDocs.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFF334155)
        )

        sampleDocs.forEach { doc ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = doc["fileName"] ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF0F172A)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = doc["status"] ?: "",
                                fontSize = 10.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = doc["summary"] ?: "",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

// ----------------- STEP 3: ANALYSIS REPORT -----------------
@Composable
fun Step3AnalysisReport(
    report: com.example.data.model.DocumentAnalysisReport,
    isAnalyzing: Boolean,
    viewModel: ExamViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "BƯỚC 3: BÁO CÁO PHÂN TÍCH TÀI LIỆU AI",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = SchoolNavy
            )

            Button(
                onClick = { viewModel.runDocumentAnalysis() },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Phân tích lại", fontSize = 12.sp)
            }
        }

        if (isAnalyzing) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = SchoolNavy)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("AI ĐANG ĐỌC TÀI LIỆU, OCR VÀ TRÍCH XUẤT MA TRẬN...", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SchoolNavy)
                    Text("Nhận diện bảng biểu • Phân tách mức độ nhận thức • Đối chiếu tính nhất quán", fontSize = 11.sp, color = Color(0xFF64748B))
                }
            }
        }

        // Analysis Stats Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(title = "Ma trận mẫu", value = "${report.matrixCount}", subtitle = "Đã chuẩn hóa", icon = Icons.Default.TableChart, color = Color(0xFF1E88E5), modifier = Modifier.weight(1f))
            MetricCard(title = "Bảng đặc tả", value = "${report.specCount}", subtitle = "Khớp 100%", icon = Icons.Default.CheckCircle, color = Color(0xFF43A047), modifier = Modifier.weight(1f))
            MetricCard(title = "Đề tham chiếu", value = "${report.sampleExamCount}", subtitle = "328+ câu hỏi", icon = Icons.Default.Description, color = Color(0xFFFB8C00), modifier = Modifier.weight(1f))
        }

        // Detailed AI Findings
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "KẾT LUẬN CỦA BỘ MÁY PHÂN TÍCH (RAG ENGINE):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SchoolNavy)
                Text(text = "• ${report.summaryNotes}", fontSize = 12.sp, color = Color(0xFF334155))
                Text(text = "• Tỉ lệ nhận thức phát hiện: ${report.cognitiveLevelsSummary}", fontSize = 12.sp, color = Color(0xFF334155))
                Text(text = "• Các chủ đề cốt lõi: ${report.topicsDetected.joinToString(", ")}", fontSize = 12.sp, color = Color(0xFF334155))
                Text(text = "• Trạng thái đồng bộ: 🟢 HỢP LỆ VÀ SẴN SÀNG SINH MA TRẬN", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ValidGreen)
            }
        }
    }
}

// ----------------- STEP 4: MATRIX -----------------
@Composable
fun Step4Matrix(
    matrixRows: List<MatrixRow>,
    project: com.example.data.model.ExamProject?,
    viewModel: ExamViewModel
) {
    val totalQuestions = matrixRows.sumOf { it.totalQuestions }
    val totalPoints = matrixRows.sumOf { it.points }
    val totalPercentage = matrixRows.sumOf { it.percentage }

    val isLocked = project?.isMatrixLocked == true
    val isValid = Math.abs(totalPoints - 10.0) < 0.05

    var editingRow by remember { mutableStateOf<MatrixRow?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "BƯỚC 4: MA TRẬN ĐỀ KIỂM TRA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = SchoolNavy
                )
                Text(
                    text = "Nguyên tắc: Ma trận là xương sống. Mọi câu hỏi đều truy ngược về ma trận.",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Lock matrix button
            OutlinedButton(
                onClick = { viewModel.toggleMatrixLock() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isLocked) SchoolNavy else Color(0xFFD97706)
                )
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isLocked) "Đã khóa ma trận" else "Khóa ma trận", fontSize = 12.sp)
            }
        }

        // Status Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (isValid) Color(0xFFE8F5E9) else Color(0xFFFEF3C7))
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isValid) "🟢 MA TRẬN HỢP LỆ" else "🟡 CẦN KIỂM TRA",
                    fontWeight = FontWeight.Bold,
                    color = if (isValid) ValidGreen else WarningYellow,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tổng số câu: $totalQuestions • Tổng điểm: $totalPoints / 10.0 • Tỉ lệ: $totalPercentage%",
                    fontSize = 12.sp,
                    color = Color(0xFF1E293B)
                )
            }
        }

        // Matrix Rows List / Table Cards
        matrixRows.forEach { row ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${row.topic}: ${row.lesson}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SchoolNavy
                        )

                        if (!isLocked) {
                            IconButton(onClick = { editingRow = row }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Sửa", modifier = Modifier.size(16.dp), tint = SchoolNavy)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Nhận biết: ${row.recognitionCount}c", fontSize = 11.sp, color = Color(0xFF1E88E5))
                        Text(text = "Thông hiểu: ${row.comprehensionCount}c", fontSize = 11.sp, color = Color(0xFF43A047))
                        Text(text = "Vận dụng: ${row.applicationCount}c", fontSize = 11.sp, color = Color(0xFFFB8C00))
                        Text(text = "VDC: ${row.highApplicationCount}c", fontSize = 11.sp, color = Color(0xFFE53935))
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Tổng: ${row.totalQuestions} câu (${row.preferredType.displayName})", fontSize = 11.sp, color = Color(0xFF64748B))
                        Text(text = "${row.points} điểm (${row.percentage}%)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFFD97706))
                    }
                }
            }
        }
    }

    if (editingRow != null) {
        val row = editingRow!!
        var nb by remember { mutableStateOf(row.recognitionCount.toString()) }
        var th by remember { mutableStateOf(row.comprehensionCount.toString()) }
        var vd by remember { mutableStateOf(row.applicationCount.toString()) }
        var vdc by remember { mutableStateOf(row.highApplicationCount.toString()) }
        var pts by remember { mutableStateOf(row.points.toString()) }

        AlertDialog(
            onDismissRequest = { editingRow = null },
            title = { Text(text = "Chỉnh sửa ma trận: ${row.topic}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nb, onValueChange = { nb = it }, label = { Text("Số câu Nhận biết") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = th, onValueChange = { th = it }, label = { Text("Số câu Thông hiểu") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = vd, onValueChange = { vd = it }, label = { Text("Số câu Vận dụng") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = vdc, onValueChange = { vdc = it }, label = { Text("Số câu Vận dụng cao") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = pts, onValueChange = { pts = it }, label = { Text("Số điểm của chủ đề") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nbInt = nb.toIntOrNull() ?: 0
                        val thInt = th.toIntOrNull() ?: 0
                        val vdInt = vd.toIntOrNull() ?: 0
                        val vdcInt = vdc.toIntOrNull() ?: 0
                        val ptsDbl = pts.toDoubleOrNull() ?: 2.5
                        val totalQ = nbInt + thInt + vdInt + vdcInt

                        viewModel.updateMatrixRow(
                            row.copy(
                                recognitionCount = nbInt,
                                comprehensionCount = thInt,
                                applicationCount = vdInt,
                                highApplicationCount = vdcInt,
                                totalQuestions = totalQ,
                                points = ptsDbl
                            )
                        )
                        editingRow = null
                    }
                ) {
                    Text("Lưu ma trận & Cập nhật đặc tả")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingRow = null }) { Text("Hủy") }
            }
        )
    }
}

// ----------------- STEP 5: SPECIFICATIONS -----------------
@Composable
fun Step5Specifications(specRows: List<SpecificationRow>, viewModel: ExamViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "BƯỚC 5: BẢNG ĐẶC TẢ ĐỀ KIỂM TRA",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SchoolNavy
        )
        Text(
            text = "Bảng đặc tả đồng bộ 100% với ma trận đề. Khi sửa ma trận, đặc tả sẽ tự động cập nhật.",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
        )

        specRows.forEach { spec ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${spec.questionCode} • ${spec.topic}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = SchoolNavy
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(spec.cognitiveLevel.colorHex).copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = spec.cognitiveLevel.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(spec.cognitiveLevel.colorHex)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "🎯 Yêu cầu cần đạt: ${spec.standardRequirement}",
                        fontSize = 12.sp,
                        color = Color(0xFF1E293B),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Dạng câu: ${spec.questionType.displayName}", fontSize = 11.sp, color = Color(0xFF64748B))
                        Text(text = "${spec.points} điểm (${spec.questionCount} câu)", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color(0xFFD97706))
                    }
                }
            }
        }
    }
}

// ----------------- STEP 6 & 7: QUESTIONS & REVIEW -----------------
@Composable
fun Step6And7Questions(questions: List<Question>, viewModel: ExamViewModel) {
    val approvedCount = questions.count { it.isApproved }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "BƯỚC 6 & 7: GIÁO VIÊN KIỂM DUYỆT CÂU HỎI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = SchoolNavy
                )
                Text(
                    text = "Tiến độ duyệt: $approvedCount / ${questions.size} câu",
                    fontSize = 11.sp,
                    color = if (approvedCount == questions.size) ValidGreen else Color(0xFFD97706),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(questions) { q ->
                QuestionCard(
                    question = q,
                    onApprove = { updated -> viewModel.updateQuestion(updated) },
                    onRegenerate = { target -> viewModel.regenerateSingleQuestion(target) },
                    onUpdate = { updated -> viewModel.updateQuestion(updated) }
                )
            }
        }
    }
}

// ----------------- STEP 8: VARIANTS -----------------
@Composable
fun Step8Variants(variants: List<com.example.data.model.ExamVariant>, viewModel: ExamViewModel) {
    var selectedVariantCount by remember { mutableStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "BƯỚC 8: TẠO NHIỀU MÃ ĐỀ",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SchoolNavy
        )
        Text(
            text = "Hệ thống tự động xáo trộn câu hỏi và các phương án A, B, C, D nhưng giữ nguyên 100% ma trận, mức độ nhận thức và độ khó tương đương giữa các mã đề.",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
        )

        // Select Count
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(1, 2, 4, 6, 8).forEach { count ->
                Button(
                    onClick = {
                        selectedVariantCount = count
                        viewModel.generateVariants(count)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedVariantCount == count) SchoolNavy else Color(0xFFE2E8F0)
                    )
                ) {
                    Text(
                        text = "$count mã",
                        color = if (selectedVariantCount == count) Color.White else SchoolNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = "BẢNG SO SÁNH TƯƠNG ĐƯƠNG GIỮA CÁC MÃ ĐỀ",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFF334155)
        )

        variants.forEach { v ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "MÃ ĐỀ: ${v.examCode}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = SchoolNavy
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🟢 ĐỘ KHÓ TƯƠNG ĐƯƠNG",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ValidGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Số lượng: ${v.questions.size} câu • Thang điểm 10.0 • Phân bố NB 40%, TH 30%, VD 20%, VDC 10%",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Mẫu đáp án 5 câu đầu: ${v.answerKey.entries.take(5).joinToString(" | ") { "${it.key}: ${it.value}" }}...",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

// ----------------- STEP 9: VALIDATION -----------------
@Composable
fun Step9Validation(
    auditItems: List<com.example.data.model.ValidationAuditItem>,
    viewModel: ExamViewModel
) {
    val allValid = auditItems.all { it.status == ValidationStatus.VALID }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "BƯỚC 9: KIỂM ĐỊNH ĐỀ & ĐỐI SOÁT MA TRẬN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = SchoolNavy
                )
                Text(
                    text = "Validation Engine kiểm tra 15 tiêu chí chất lượng trước khi cho phép xuất đề.",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Button(
                onClick = { viewModel.runValidationAudit() },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Đối soát lại", fontSize = 11.sp)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (allValid) Color(0xFFE8F5E9) else Color(0xFFFEF3C7))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (allValid) ValidGreen else WarningYellow,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (allValid) "ĐỀ ĐÃ ĐƯỢC KIỂM TRA VÀ ĐẠT CHUẨN" else "CÓ ĐIỂM CẦN KIỂM TRA LẠI",
                        fontWeight = FontWeight.Bold,
                        color = if (allValid) ValidGreen else WarningYellow,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Tất cả các tiêu chí số câu, thang điểm, chuẩn kiến thức và ma trận đều khớp 100%.",
                        fontSize = 11.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            }
        }

        Text(
            text = "BẢNG ĐỐI SOÁT CHI TIẾT (MA TRẬN VS ĐỀ THỰC TẾ)",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFF334155)
        )

        auditItems.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.criteria, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = item.notes, fontSize = 11.sp, color = Color(0xFF64748B))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Đề: ${item.actualExam}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SchoolNavy)
                            Text(text = "Chuẩn: ${item.matrixTarget}", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = item.status.symbol, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

// ----------------- STEP 10: EXPORT -----------------
@Composable
fun Step10Export(
    project: com.example.data.model.ExamProject?,
    variants: List<com.example.data.model.ExamVariant>,
    questions: List<Question>,
    viewModel: ExamViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "BƯỚC 10: XUẤT FILE & IN ẤN",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SchoolNavy
        )
        Text(
            text = "Đầy đủ khung tiêu đề trường THCS Long Xuyên, bảng ma trận, bảng đặc tả, đề thi theo từng mã đề và hướng dẫn chấm chi tiết.",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
        )

        // Export Action Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "CÁC ĐỊNH DẠNG XUẤT BẢN CHUẨN SƯ PHẠM:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SchoolNavy)

                Button(
                    onClick = { viewModel.navigateTo(AppScreen.EXAM_PREVIEW) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text("📄 Xuất file Word (.DOCX) – Đầy đủ khung trường THCS Long Xuyên")
                }

                Button(
                    onClick = { viewModel.navigateTo(AppScreen.EXAM_PREVIEW) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("📊 Xuất file Excel (.XLSX) – Ma trận, đặc tả & ngân hàng câu")
                }

                Button(
                    onClick = { viewModel.navigateTo(AppScreen.EXAM_PREVIEW) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("📕 Xuất file PDF & Lệnh in – Đề học sinh + Hướng dẫn chấm")
                }
            }
        }

        // Preview of Official School Header for Exam
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "UBND THÀNH PHỐ LONG XUYÊN", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Text(text = "TRƯỜNG THCS LONG XUYÊN", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = SchoolNavy)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = project?.title ?: "ĐỀ KIỂM TRA CHÍNH THỨC", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                Text(text = "Thời gian làm bài: ${project?.durationMinutes ?: 60} phút (không kể thời gian phát đề)", fontSize = 11.sp, color = Color(0xFF64748B))
            }
        }
    }
}
