package com.example.mymoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymoney.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    message: String,
    isLoading: Boolean = false,
    onReset: (String) -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isSent by remember { mutableStateOf(false) }
    var localMessage by remember(message) { mutableStateOf(message) }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            localMessage = message
            if (message.contains("thành công", ignoreCase = true) || message.contains("Đã gửi", ignoreCase = true)) {
                isSent = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VcnSoftGray)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("VCN", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = VcnTextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Khôi phục mật khẩu", style = MaterialTheme.typography.bodySmall, color = VcnTextSecondary)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isSent) {
                    Text(
                        text = "Đã gửi email khôi phục!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = VcnGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Đã gửi liên kết đặt lại mật khẩu đến:\n$email\n\nVui lòng kiểm tra hộp thư (hoặc Spam) để đổi mật khẩu.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = VcnTextSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Quay lại đăng nhập", fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        onClick = {
                            isSent = false
                            localMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Nhập email khác / Gửi lại", color = VcnOrange)
                    }
                } else {
                    Text(
                        text = "Quên mật khẩu",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = VcnTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nhập email đã đăng ký để nhận liên kết đặt lại mật khẩu.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = VcnTextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (localMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = localMessage, color = VcnOrange, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                localMessage = "Vui lòng nhập email."
                            } else {
                                localMessage = "Đang gửi email..."
                                onReset(email.trim())
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Gửi email đặt lại mật khẩu", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Quay lại đăng nhập", color = VcnTextSecondary)
                    }
                }
            }
        }
    }
}
