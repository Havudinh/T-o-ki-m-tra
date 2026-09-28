package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GradeLevel
import com.example.data.model.SubjectType
import com.example.ui.theme.SchoolNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickBuilderDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        subject: SubjectType,
        grade: GradeLevel,
        duration: Int,
        scope: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("Đề kiểm tra nhanh THCS Long Xuyên") }
    var selectedSubject by remember { mutableStateOf(SubjectType.KHTN) }
    var selectedGrade by remember { mutableStateOf(GradeLevel.GRADE_8) }
    var duration by remember { mutableStateOf("45") }
    var scope by remember { mutableStateOf("Kiểm tra 1 tiết: Phản ứng hóa học, Định luật bảo toàn khối lượng và Áp suất") }

    var subjectExpanded by remember { mutableStateOf(false) }
    var gradeExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "⚡ TẠO ĐỀ NHANH",
                fontWeight = FontWeight.Bold,
                color = SchoolNavy,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "AI sẽ tự động khởi tạo Ma trận chuẩn → Bảng đặc tả → Bộ câu hỏi → Đáp án theo phân phối chương trình THCS.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên đề kiểm tra") },
                    modifier = Modifier.fillMaxWidth().testTag("quick_title_input")
                )

                // Select Subject Dropdown
                ExposedDropdownMenuBox(
                    expanded = subjectExpanded,
                    onExpandedChange = { subjectExpanded = !subjectExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSubject.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Chọn môn học") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = subjectExpanded,
                        onDismissRequest = { subjectExpanded = false }
                    ) {
                        SubjectType.values().forEach { subj ->
                            DropdownMenuItem(
                                text = { Text(subj.displayName) },
                                onClick = {
                                    selectedSubject = subj
                                    subjectExpanded = false
                                }
                            )
                        }
                    }
                }

                // Select Grade Dropdown
                ExposedDropdownMenuBox(
                    expanded = gradeExpanded,
                    onExpandedChange = { gradeExpanded = !gradeExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedGrade.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Chọn khối lớp") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = gradeExpanded,
                        onDismissRequest = { gradeExpanded = false }
                    ) {
                        GradeLevel.values().take(4).forEach { gr ->
                            DropdownMenuItem(
                                text = { Text(gr.displayName) },
                                onClick = {
                                    selectedGrade = gr
                                    gradeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Duration
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Thời lượng (phút)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Scope description
                OutlinedTextField(
                    value = scope,
                    onValueChange = { scope = it },
                    label = { Text("Nội dung phạm vi kiểm tra") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val durInt = duration.toIntOrNull() ?: 45
                    onSubmit(title, selectedSubject, selectedGrade, durInt, scope)
                },
                modifier = Modifier.testTag("btn_confirm_quick_build"),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Tạo ngay")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
