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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.GeminiService
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.SchoolNavyDark
import com.example.ui.theme.ValidGreen
import com.example.ui.viewmodel.ExamViewModel

@Composable
fun SettingsScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.goToHome() }

    var schoolName by remember { mutableStateOf("TRƯỜNG THCS LONG XUYÊN") }
    var deptName by remember { mutableStateOf("Tổ Khoa học tự nhiên - Toán") }
    var slogan by remember { mutableStateOf("Ma trận chuẩn – Đặc tả đồng bộ – Câu hỏi chất lượng – Đề kiểm tra thông minh") }
    var teacherName by remember { mutableStateOf("Giáo viên THCS Long Xuyên") }

    val isApiConfigured = GeminiService.isApiKeyConfigured()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "CẤU HÌNH NHẬN DIỆN & HỆ THỐNG",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SchoolNavy
        )

        // School Identity Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SchoolNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.School, contentDescription = null, tint = SchoolGold)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "THÔNG TIN ĐƠN VỊ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SchoolNavy)
                        Text(text = "Được in trên tiêu đề mọi đề kiểm tra và đáp án", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                OutlinedTextField(
                    value = schoolName,
                    onValueChange = { schoolName = it },
                    label = { Text("Tên trường") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = deptName,
                    onValueChange = { deptName = it },
                    label = { Text("Tổ bộ môn chuyên môn") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = { Text("Tên giáo viên phụ trách") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = slogan,
                    onValueChange = { slogan = it },
                    label = { Text("Khẩu hiệu sư phạm") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        }

        // AI Engine & API Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "TRẠNG THÁI KẾT NỐI AI / GEMINI", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SchoolNavy)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isApiConfigured) Color(0xFFE8F5E9) else Color(0xFFFEF3C7))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isApiConfigured) "🟢 ĐÃ KẾT NỐI API" else "🟡 CHẾ ĐỘ DEMO",
                            color = if (isApiConfigured) ValidGreen else Color(0xFFD97706),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = if (isApiConfigured)
                        "Ứng dụng đang sử dụng mô hình gemini-3.5-flash trực tiếp qua BuildConfig. Sinh đề và phân tích thời gian thực."
                    else
                        "Chế độ Demo thông minh: Tích hợp sẵn toàn bộ chương trình GDPT 2018 THCS Long Xuyên, ngân hàng câu hỏi chuẩn hóa, ma trận và đặc tả mà không phụ thuộc internet.",
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 16.sp
                )
            }
        }

        Button(
            onClick = { viewModel.goToHome() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Lưu thông tin & Trở về")
        }
    }
}
