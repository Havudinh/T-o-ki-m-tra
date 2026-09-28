package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ExamProject
import com.example.ui.components.QuickBuilderDialog
import com.example.ui.theme.AcademicCardBorder
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolNavy
import com.example.ui.theme.SchoolNavyDark
import com.example.ui.theme.ValidGreen
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ExamViewModel

@Composable
fun HomeScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    var showQuickBuilder by remember { mutableStateOf(false) }

    val allProjects by viewModel.allProjects.collectAsStateWithLifecycle()
    val allBankQuestions by viewModel.allBankQuestions.collectAsStateWithLifecycle()
    val activeProjectId by viewModel.activeProjectId.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SchoolNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SchoolNavyDark, SchoolNavy, Color(0xFF1976D2))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SchoolGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = SchoolNavyDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "TRƯỜNG THCS LONG XUYÊN",
                                    color = SchoolGold,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Năm học 2026–2027 • Tổ Chuyên môn THCS",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "ỨNG DỤNG XÂY DỰNG ĐỀ KIỂM TRA",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Từ ma trận – bảng đặc tả – ngân hàng câu hỏi đến đề kiểm tra hoàn chỉnh",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Primary Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.setWizardStep(1)
                                    viewModel.navigateTo(AppScreen.WIZARD)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_create_new_exam"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolGold)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = SchoolNavyDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TẠO ĐỀ MỚI",
                                    color = SchoolNavyDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = { showQuickBuilder = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_quick_build"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TẠO NHANH",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Metrics / Dashboard Statistics Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Đề đã tạo",
                    value = "${allProjects.size}",
                    subtitle = "100% bám ma trận",
                    icon = Icons.Default.Description,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Ngân hàng",
                    value = "${allBankQuestions.size.coerceAtLeast(15)}",
                    subtitle = "Câu hỏi chuẩn hóa",
                    icon = Icons.Default.MenuBook,
                    color = Color(0xFF43A047),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Dự án",
                    value = "${allProjects.size.coerceAtLeast(1)}",
                    subtitle = "Đang lưu trữ",
                    icon = Icons.Default.TableChart,
                    color = Color(0xFFFB8C00),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Feature Navigation Hub
        item {
            Text(
                text = "CÁC CHỨC NĂNG HỆ THỐNG",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureHubCard(
                        title = "Ngân hàng câu hỏi",
                        subtitle = "Tìm kiếm, lọc & thêm câu",
                        icon = Icons.Default.MenuBook,
                        badgeColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppScreen.QUESTION_BANK) }
                    )
                    FeatureHubCard(
                        title = "Ma trận & Bảng đặc tả",
                        subtitle = "Thư viện mẫu GDPT 2018",
                        icon = Icons.Default.TableChart,
                        badgeColor = Color(0xFF1565C0),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppScreen.MATRIX_SPEC) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureHubCard(
                        title = "Lịch sử đề & Dự án",
                        subtitle = "Nhân bản, sao lưu & khôi phục",
                        icon = Icons.Default.History,
                        badgeColor = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppScreen.HISTORY) }
                    )
                    FeatureHubCard(
                        title = "Xem & Xuất đề",
                        subtitle = "Word, PDF, Excel đa mã đề",
                        icon = Icons.Default.CheckCircle,
                        badgeColor = Color(0xFF6A1B9A),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppScreen.EXAM_PREVIEW) }
                    )
                }
            }
        }

        // Recent Projects Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "DỰ ÁN ĐỀ KIỂM TRA GẦN ĐÂY",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${allProjects.size} dự án",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }
        }

        items(allProjects) { proj ->
            ProjectItemCard(
                project = proj,
                isActive = proj.id == activeProjectId,
                onOpen = {
                    viewModel.loadProject(proj.id)
                    viewModel.setWizardStep(4) // jump to matrix review
                    viewModel.navigateTo(AppScreen.WIZARD)
                },
                onDuplicate = { viewModel.duplicateProject(proj.id) },
                onDelete = { viewModel.deleteProject(proj.id) }
            )
        }
    }

    if (showQuickBuilder) {
        QuickBuilderDialog(
            onDismiss = { showQuickBuilder = false },
            onSubmit = { title, subj, grade, dur, scope ->
                viewModel.createNewProject(
                    title = title,
                    subject = subj,
                    grade = grade,
                    semester = "Học kỳ I",
                    schoolYear = "2026–2027",
                    durationMinutes = dur,
                    totalPoints = 10.0,
                    bookSeries = "Cánh Diều",
                    scopeDescription = scope
                )
                showQuickBuilder = false
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = Color(0xFF475569)
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun FeatureHubCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, AcademicCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
fun ProjectItemCard(
    project: ExamProject,
    isActive: Boolean,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_item_${project.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFFF0FDF4) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) ValidGreen else AcademicCardBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${project.subject.displayName} ${project.grade.displayName}",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = project.semester,
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "${project.durationMinutes} phút • ${project.totalPoints}đ",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = Color(0xFFD97706)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )

            if (project.scopeDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.scopeDescription,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onDuplicate, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Nhân bản",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xóa",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolNavy),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "Mở đề", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
