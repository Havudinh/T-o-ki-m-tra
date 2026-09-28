package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CognitiveLevel
import com.example.data.model.Question
import com.example.data.model.SubjectType
import com.example.ui.components.QuestionCard
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolNavy
import com.example.ui.viewmodel.ExamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBankScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.goToHome() }

    val allQuestions by viewModel.allBankQuestions.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedFilterSubject.collectAsStateWithLifecycle()
    val selectedLevel by viewModel.selectedFilterLevel.collectAsStateWithLifecycle()

    var subjectMenuExpanded by remember { mutableStateOf(false) }

    val filteredQuestions = allQuestions.filter { q ->
        val matchesQuery = searchQuery.isBlank() ||
                q.content.contains(searchQuery, ignoreCase = true) ||
                q.topic.contains(searchQuery, ignoreCase = true) ||
                q.questionCode.contains(searchQuery, ignoreCase = true)
        val matchesSubject = selectedSubject == null || q.subject == selectedSubject
        val matchesLevel = selectedLevel == null || q.cognitiveLevel == selectedLevel
        matchesQuery && matchesSubject && matchesLevel
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "NGÂN HÀNG CÂU HỎI CHUẨN HÓA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = SchoolNavy
                )
                Text(
                    text = "Hiển thị ${filteredQuestions.size} câu hỏi theo bộ lọc",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_question_bank_input"),
            placeholder = { Text("Tìm theo nội dung, chủ đề, mã câu...", fontSize = 12.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(10.dp)
        )

        // Filter chips (Subject & Level)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = subjectMenuExpanded,
                onExpandedChange = { subjectMenuExpanded = !subjectMenuExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedSubject?.displayName ?: "Tất cả môn",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Môn học") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = subjectMenuExpanded,
                    onDismissRequest = { subjectMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Tất cả môn") },
                        onClick = {
                            viewModel.setFilterSubject(null)
                            subjectMenuExpanded = false
                        }
                    )
                    SubjectType.values().take(6).forEach { subj ->
                        DropdownMenuItem(
                            text = { Text(subj.displayName) },
                            onClick = {
                                viewModel.setFilterSubject(subj)
                                subjectMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Quick level filter buttons
            Row(
                modifier = Modifier.weight(1.2f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    null to "Tất cả",
                    CognitiveLevel.RECOGNITION to "NB",
                    CognitiveLevel.COMPREHENSION to "TH",
                    CognitiveLevel.APPLICATION to "VD"
                ).forEach { (lvl, lbl) ->
                    val isSelected = selectedLevel == lvl
                    Button(
                        onClick = { viewModel.setFilterLevel(lvl) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) SchoolNavy else Color(0xFFE2E8F0)
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                    ) {
                        Text(
                            text = lbl,
                            color = if (isSelected) Color.White else SchoolNavy,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Question List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (filteredQuestions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Không tìm thấy câu hỏi phù hợp", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = "Vui lòng điều chỉnh từ khóa tìm kiếm hoặc bỏ chọn bộ lọc.", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            } else {
                items(filteredQuestions) { q ->
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
}
