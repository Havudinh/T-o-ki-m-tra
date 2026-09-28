package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ExamProject
import com.example.data.model.ExamVariant
import com.example.data.model.MatrixRow
import com.example.data.model.Question
import com.example.data.model.SpecificationRow
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.ValidGreen
import com.example.ui.viewmodel.ExamViewModel

enum class PreviewMode(val displayName: String) {
    STUDENT("Đề học sinh"),
    TEACHER("Đề giáo viên & Hướng dẫn chấm"),
    ANSWER_KEY("Bảng đáp án các mã đề"),
    MATRIX_SPEC("Ma trận & Bảng đặc tả")
}

@Composable
fun ExamPreviewScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.goToHome() }

    val context = LocalContext.current
    val project: ExamProject? by viewModel.currentProject.collectAsStateWithLifecycle()
    val variants: List<ExamVariant> by viewModel.examVariants.collectAsStateWithLifecycle()
    val questions: List<Question> by viewModel.questions.collectAsStateWithLifecycle()
    val matrixRows: List<MatrixRow> by viewModel.matrixRows.collectAsStateWithLifecycle()
    val specRows: List<SpecificationRow> by viewModel.specRows.collectAsStateWithLifecycle()

    var selectedMode by remember { mutableStateOf(PreviewMode.STUDENT) }
    var selectedVariantIndex by remember { mutableStateOf(0) }

    val currentVariant = variants.getOrNull(selectedVariantIndex)
    val displayQuestions: List<Question> = currentVariant?.questions ?: questions

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mode Selector Tabs
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            TabRow(
                selectedTabIndex = selectedMode.ordinal,
                containerColor = Color.White,
                contentColor = SchoolNavy
            ) {
                for (mode in PreviewMode.values()) {
                    Tab(
                        selected = selectedMode == mode,
                        onClick = { selectedMode = mode },
                        text = {
                            Text(
                                text = mode.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (selectedMode == mode) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Exam Variant Switcher (if multi-variant mode)
        if (variants.isNotEmpty() && selectedMode != PreviewMode.ANSWER_KEY && selectedMode != PreviewMode.MATRIX_SPEC) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Chọn mã đề:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SchoolNavy)
                for (idx in variants.indices) {
                    val v = variants[idx]
                    val isSelected = selectedVariantIndex == idx
                    Button(
                        onClick = { selectedVariantIndex = idx },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) SchoolNavy else Color(0xFFE2E8F0)
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Mã ${v.examCode}",
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White else SchoolNavy,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Actions: Copy all text & Export Word / PDF
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val fullText = buildExportText(project, displayQuestions, selectedMode, currentVariant?.examCode ?: "101")
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Đề kiểm tra THCS Long Xuyên", fullText))
                    Toast.makeText(context, "Đã sao chép nội dung xuất bản!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_copy_exam_text"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy)
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sao chép (.DOCX)", fontSize = 11.sp)
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Lệnh in ấn: Đang chuẩn bị tệp PDF xuất bản...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("In ấn / PDF", fontSize = 11.sp)
            }
        }

        // Main Paper View Container
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Official School Header
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text("UBND THÀNH PHỐ LONG XUYÊN", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("TRƯỜNG THCS LONG XUYÊN", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = SchoolNavy)
                                Text("TỔ KHOA HỌC TỰ NHIÊN - TOÁN", fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("ĐỀ KIỂM TRA ĐỊNH KỲ", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("NĂM HỌC 2026 – 2027", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("MÃ ĐỀ: ${currentVariant?.examCode ?: "101"}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color(0xFFD97706))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = project?.title?.uppercase() ?: "ĐỀ KIỂM TRA",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = SchoolNavy
                        )
                        Text(
                            text = "Môn: ${project?.subject?.displayName} • ${project?.grade?.displayName} • Thời gian: ${project?.durationMinutes ?: 60} phút",
                            fontSize = 11.sp,
                            color = Color(0xFF334155)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        // Student Name Box Mockup
                        if (selectedMode == PreviewMode.STUDENT) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Họ và tên: .................................................................", fontSize = 11.sp)
                                    Text("Lớp: .............. SBD: ..............", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                when (selectedMode) {
                    PreviewMode.STUDENT -> {
                        items(displayQuestions) { q ->
                            Column(modifier = Modifier.padding(bottom = 6.dp)) {
                                Text(
                                    text = "${q.questionCode} (${q.points}đ): ${q.content}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                                for (opt in q.getOptionsList()) {
                                    Text(
                                        text = "   $opt",
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    PreviewMode.TEACHER -> {
                        items(displayQuestions) { q ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "${q.questionCode} [${q.cognitiveLevel.shortName} - ${q.points}đ]: ${q.content}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                for (opt in q.getOptionsList()) {
                                    Text(text = "   $opt", fontSize = 11.sp, color = Color(0xFF475569))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "✔ Đáp án: ${q.correctAnswer}",
                                    fontWeight = FontWeight.Bold,
                                    color = ValidGreen,
                                    fontSize = 11.sp
                                )
                                if (q.explanation.isNotBlank()) {
                                    Text(text = "💡 Giải thích: ${q.explanation}", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                if (q.getRubricList().isNotEmpty()) {
                                    Text(text = "📋 Hướng dẫn chấm (Rubric):", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    for ((crit, pts) in q.getRubricList()) {
                                        Text(text = "   • $crit: $pts điểm", fontSize = 10.sp, color = Color(0xFF475569))
                                    }
                                }
                            }
                        }
                    }

                    PreviewMode.ANSWER_KEY -> {
                        item {
                            Text(text = "BẢNG ĐÁP ÁN TỔNG HỢP CÁC MÃ ĐỀ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SchoolNavy)
                        }
                        items(variants) { v ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "MÃ ĐỀ ${v.examCode}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SchoolNavy)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    val keyList = v.answerKey.entries.toList()
                                    val chunks = keyList.chunked(5)
                                    for (chunk in chunks) {
                                        Text(
                                            text = chunk.joinToString("   |   ") { "${it.key}: ${it.value}" },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF1E293B)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    PreviewMode.MATRIX_SPEC -> {
                        item {
                            Text(text = "MA TRẬN & BẢNG ĐẶC TẢ PHÊ DUYỆT", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SchoolNavy)
                        }
                        items(matrixRows) { row ->
                            Text(
                                text = "• ${row.topic} (${row.lesson}): NB ${row.recognitionCount}c | TH ${row.comprehensionCount}c | VD ${row.applicationCount}c | VDC ${row.highApplicationCount}c => ${row.points}đ",
                                fontSize = 11.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun buildExportText(
    project: ExamProject?,
    questions: List<Question>,
    mode: PreviewMode,
    examCode: String
): String {
    val sb = StringBuilder()
    sb.appendLine("UBND THÀNH PHỐ LONG XUYÊN")
    sb.appendLine("TRƯỜNG THCS LONG XUYÊN - TỔ CHUYÊN MÔN")
    sb.appendLine("ĐỀ KIỂM TRA ĐỊNH KỲ NĂM HỌC 2026–2027")
    sb.appendLine("Tên bài: ${project?.title ?: "Đề kiểm tra"}")
    sb.appendLine("Môn: ${project?.subject?.displayName} - ${project?.grade?.displayName}")
    sb.appendLine("Thời gian: ${project?.durationMinutes ?: 60} phút | Mã đề: $examCode")
    sb.appendLine("========================================================\n")

    for (q in questions) {
        sb.appendLine("${q.questionCode} (${q.points} điểm): ${q.content}")
        for (opt in q.getOptionsList()) {
            sb.appendLine("   $opt")
        }
        if (mode == PreviewMode.TEACHER) {
            sb.appendLine("   => Đáp án đúng: ${q.correctAnswer}")
            sb.appendLine("   => Hướng dẫn giải: ${q.explanation}")
            if (q.getRubricList().isNotEmpty()) {
                sb.appendLine("   => Hướng dẫn chấm:")
                for ((crit, pts) in q.getRubricList()) {
                    sb.appendLine("      • $crit: $pts điểm")
                }
            }
        }
        sb.appendLine()
    }
    return sb.toString()
}
