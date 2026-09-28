package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import com.example.data.model.CognitiveLevel
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.ValidGreen

@Composable
fun QuestionCard(
    question: Question,
    onApprove: (Question) -> Unit,
    onRegenerate: (Question) -> Unit,
    onUpdate: (Question) -> Unit,
    modifier: Modifier = Modifier
) {
    var showWhyDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("question_card_${question.questionCode}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(
            width = if (question.isApproved) 1.5.dp else 1.dp,
            color = if (question.isApproved) ValidGreen else AcademicCardBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Question Code, Cognitive Level Chip, Type, Points
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SchoolNavy)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = question.questionCode,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Cognitive Level Chip
                    val levelBg = Color(question.cognitiveLevel.colorHex)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(levelBg.copy(alpha = 0.15f))
                            .border(1.dp, levelBg.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${question.cognitiveLevel.shortName} – ${question.cognitiveLevel.displayName}",
                            color = levelBg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Points & Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${question.points} điểm",
                        color = Color(0xFFD97706),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    if (question.isApproved) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Đã duyệt",
                            tint = ValidGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Topic & Lesson
            Text(
                text = "${question.topic} • ${question.lesson}",
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Question Content
            Text(
                text = question.content,
                color = Color(0xFF1E293B),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            )

            // Multiple Choice Options (if applicable)
            val options = question.getOptionsList()
            if (options.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    options.forEach { opt ->
                        val isCorrect = opt.startsWith("${question.correctAnswer}.") || opt.startsWith("${question.correctAnswer} ")
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isCorrect) ValidGreen.copy(alpha = 0.6f) else Color(0xFFE2E8F0)
                            )
                        ) {
                            Text(
                                text = opt,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                color = if (isCorrect) Color(0xFF1B5E20) else Color(0xFF334155),
                                fontSize = 13.sp,
                                fontWeight = if (isCorrect) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Short Answer / True-False Answer Info
            if (question.questionType != QuestionType.MULTIPLE_CHOICE_4) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Đáp án chuẩn: ${question.correctAnswer}",
                            color = Color(0xFF0F172A),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        if (question.getRubricList().isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Thang điểm (Rubric):",
                                color = Color(0xFF475569),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            question.getRubricList().forEach { (crit, pts) ->
                                Text(
                                    text = "• $crit ($pts đ)",
                                    color = Color(0xFF475569),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Explanation Section
            if (question.explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💡 Lời giải / Hướng dẫn: ${question.explanation}",
                    color = Color(0xFF475569),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Pedagogical "Vì sao?" button
                TextButton(
                    onClick = { showWhyDialog = true },
                    modifier = Modifier.testTag("btn_why_${question.questionCode}")
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Vì sao?",
                        modifier = Modifier.size(16.dp),
                        tint = SchoolNavy
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Vì sao?", fontSize = 12.sp, color = SchoolNavy)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Edit Question
                    OutlinedButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("btn_edit_${question.questionCode}"),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Sửa câu",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Sửa", fontSize = 12.sp)
                    }

                    // Regenerate Alternative Question
                    OutlinedButton(
                        onClick = { onRegenerate(question) },
                        modifier = Modifier.testTag("btn_regen_${question.questionCode}"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1565C0)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sinh câu khác",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Sinh câu khác", fontSize = 12.sp)
                    }

                    // Approve Question
                    Button(
                        onClick = { onApprove(question.copy(isApproved = !question.isApproved)) },
                        modifier = Modifier.testTag("btn_approve_${question.questionCode}"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (question.isApproved) ValidGreen else SchoolNavy
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Giữ câu",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (question.isApproved) "Đã duyệt" else "Giữ câu này",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    // "Vì sao?" Dialog (Chế độ AI giải thích cơ sở khoa học & sư phạm)
    if (showWhyDialog) {
        AlertDialog(
            onDismissRequest = { showWhyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SchoolNavy
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cơ sở sư phạm câu ${question.questionCode}")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "📌 Mức độ nhận thức: ${question.cognitiveLevel.displayName}",
                        fontWeight = FontWeight.Bold,
                        color = Color(question.cognitiveLevel.colorHex)
                    )
                    Text(
                        text = "🎯 Yêu cầu cần đạt: Bám sát chuẩn GDPT 2018 môn ${question.subject.displayName} ${question.grade.displayName}.",
                        fontSize = 13.sp
                    )
                    Text(
                        text = "📖 Nguồn kiến thức: ${question.knowledgeSource}",
                        fontSize = 13.sp,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "💡 Giải thích xếp loại: ${if (question.pedagogicalReason.isNotBlank()) question.pedagogicalReason else "Câu hỏi được thiết kế để đánh giá đúng năng lực cốt lõi theo ma trận đã duyệt, không chứa dữ kiện thừa gây nhiễu vô nghĩa."}",
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showWhyDialog = false }) {
                    Text("Đã hiểu")
                }
            }
        )
    }

    // Edit Question Dialog
    if (showEditDialog) {
        var editContent by remember { mutableStateOf(question.content) }
        var editAnswer by remember { mutableStateOf(question.correctAnswer) }
        var editExplanation by remember { mutableStateOf(question.explanation) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text(text = "Chỉnh sửa câu hỏi ${question.questionCode}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("Nội dung câu hỏi") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    OutlinedTextField(
                        value = editAnswer,
                        onValueChange = { editAnswer = it },
                        label = { Text("Đáp án đúng") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editExplanation,
                        onValueChange = { editExplanation = it },
                        label = { Text("Lời giải / Hướng dẫn") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdate(
                            question.copy(
                                content = editContent,
                                correctAnswer = editAnswer,
                                explanation = editExplanation,
                                isApproved = true
                            )
                        )
                        showEditDialog = false
                    }
                ) {
                    Text("Lưu thay đổi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
