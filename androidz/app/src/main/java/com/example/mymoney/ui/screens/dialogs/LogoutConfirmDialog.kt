package com.example.mymoney.ui.screens.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymoney.ui.theme.*

@Composable
fun LogoutConfirmDialog(
    userDisplayName: String?,
    userEmail: String?,
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    val displayName = userDisplayName?.takeIf { it.isNotBlank() } ?: "ABC"
    val displayEmail = userEmail?.takeIf { it.isNotBlank() } ?: "abc@gmail.com"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VcnSoftGray)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = VcnCardSurface,
                    shadowElevation = 1.dp,
                    modifier = Modifier.size(38.dp)
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = VcnTextPrimary)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Đăng xuất",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VcnTextPrimary
                )
            }
            Surface(
                shape = CircleShape,
                color = VcnCardSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.size(38.dp)
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = VcnTextPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Main Card
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(VcnOrangeLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = VcnOrange,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bạn muốn đăng xuất?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = VcnTextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Bạn sẽ thoát khỏi phiên đăng nhập hiện tại và có thể đăng nhập lại bất kỳ lúc nào.",
                    style = MaterialTheme.typography.bodySmall,
                    color = VcnTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // User summary row
                Surface(
                    color = VcnSoftGray,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(VcnOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(displayName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = VcnTextPrimary)
                            Text("$displayEmail • Đã đồng bộ", style = MaterialTheme.typography.labelSmall, color = VcnTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Safe data notice
                Surface(
                    color = VcnGreenBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VcnGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Dữ liệu của bạn được giữ an toàn",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = VcnGreen
                            )
                            Text(
                                text = "Các giao dịch, ví và ngân sách sẽ không bị mất khi đăng xuất.",
                                style = MaterialTheme.typography.labelSmall,
                                color = VcnGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onConfirmLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đăng xuất khỏi tài khoản", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(1.dp, VcnDividerGray),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("Hủy, quay lại Cá nhân", color = VcnTextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Bạn có thể đăng nhập lại bất cứ lúc nào bằng email đã đăng ký.",
            style = MaterialTheme.typography.labelSmall,
            color = VcnTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "VCN",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = VcnTextPrimary
        )
        Text(
            text = "Quản lý chi tiêu cá nhân",
            style = MaterialTheme.typography.labelSmall,
            color = VcnTextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
