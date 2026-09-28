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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MatrixRow
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.ValidGreen
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ExamViewModel

@Composable
fun MatrixSpecScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.goToHome() }

    val matrixRows by viewModel.matrixRows.collectAsStateWithLifecycle()
    val specRows by viewModel.specRows.collectAsStateWithLifecycle()
    val project by viewModel.currentProject.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "THƯ VIỆN MA TRẬN & BẢNG ĐẶC TẢ CHUẨN",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = SchoolNavy
            )
            Text(
                text = "Cấu trúc ma trận theo Công văn hướng dẫn của Bộ GD&ĐT và Trường THCS Long Xuyên",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }

        // Active Project Matrix Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = project?.title ?: "Đề hiện tại",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SchoolNavy
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Đồng bộ 100%",
                                color = ValidGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Khối: ${project?.grade?.displayName} • Môn: ${project?.subject?.displayName} • Kỳ: ${project?.semester} • Thang điểm: 10.0",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.setWizardStep(4)
                            viewModel.navigateTo(AppScreen.WIZARD)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chỉnh sửa ma trận đề này", fontSize = 12.sp)
                    }
                }
            }
        }

        // Matrix Rows
        item {
            Text(
                text = "CÁC CHỦ ĐỀ TRONG MA TRẬN (${matrixRows.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF334155)
            )
        }

        items(matrixRows) { row ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "${row.topic}: ${row.lesson}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SchoolNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "NB: ${row.recognitionCount}c", fontSize = 11.sp, color = Color(0xFF1E88E5))
                        Text(text = "TH: ${row.comprehensionCount}c", fontSize = 11.sp, color = Color(0xFF43A047))
                        Text(text = "VD: ${row.applicationCount}c", fontSize = 11.sp, color = Color(0xFFFB8C00))
                        Text(text = "VDC: ${row.highApplicationCount}c", fontSize = 11.sp, color = Color(0xFFE53935))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Điểm: ${row.points}đ (${row.percentage}%) • Tổng: ${row.totalQuestions} câu", fontSize = 11.sp, color = Color(0xFF64748B))
                }
            }
        }

        // Specifications
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "BẢNG ĐẶC TẢ CHI TIẾT (${specRows.size} MỤC)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF334155)
            )
        }

        items(specRows) { spec ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${spec.questionCode} • ${spec.topic}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SchoolNavy)
                        Text(text = "${spec.cognitiveLevel.displayName} (${spec.points}đ)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(spec.cognitiveLevel.colorHex))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = spec.standardRequirement, fontSize = 11.sp, color = Color(0xFF334155))
                }
            }
        }
    }
}
