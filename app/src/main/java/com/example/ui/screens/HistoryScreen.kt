package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolNavy
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ExamViewModel

@Composable
fun HistoryScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.goToHome() }

    val allProjects by viewModel.allProjects.collectAsStateWithLifecycle()
    val activeId by viewModel.activeProjectId.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "LỊCH SỬ ĐỀ & QUẢN LÝ DỰ ÁN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SchoolNavy
                    )
                    Text(
                        text = "Lưu trữ ma trận, bảng đặc tả, bộ câu hỏi và các mã đề",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Button(
                    onClick = {
                        viewModel.setWizardStep(1)
                        viewModel.navigateTo(AppScreen.WIZARD)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Dự án mới", fontSize = 12.sp)
                }
            }
        }

        if (allProjects.isEmpty()) {
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
                        Text(text = "Chưa có dự án nào", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                }
            }
        } else {
            items(allProjects) { proj ->
                ProjectItemCard(
                    project = proj,
                    isActive = proj.id == activeId,
                    onOpen = {
                        viewModel.loadProject(proj.id)
                        viewModel.setWizardStep(4)
                        viewModel.navigateTo(AppScreen.WIZARD)
                    },
                    onDuplicate = { viewModel.duplicateProject(proj.id) },
                    onDelete = { viewModel.deleteProject(proj.id) }
                )
            }
        }
    }
}
