package com.example.mymoney

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymoney.ui.navigation.AppTab
import com.example.mymoney.ui.screens.ForgotPasswordScreen
import com.example.mymoney.ui.screens.LoginScreen
import com.example.mymoney.ui.screens.RegisterScreen
import com.example.mymoney.ui.screens.dialogs.AddTransactionSheet
import com.example.mymoney.ui.screens.dialogs.LogoutConfirmDialog
import com.example.mymoney.ui.screens.dialogs.ReportDetailDialog
import com.example.mymoney.ui.screens.tabs.BudgetScreen
import com.example.mymoney.ui.screens.tabs.NextMonthPlanScreen
import com.example.mymoney.ui.screens.tabs.OverviewScreen
import com.example.mymoney.ui.screens.tabs.ReportsScreen
import com.example.mymoney.ui.screens.tabs.SettingsScreen
import com.example.mymoney.ui.screens.tabs.TransactionsScreen
import com.example.mymoney.ui.theme.VcnCardSurface
import com.example.mymoney.ui.theme.VcnDividerGray
import com.example.mymoney.ui.theme.VcnOrange
import com.example.mymoney.ui.theme.VcnSoftGray
import com.example.mymoney.ui.theme.VcnTextSecondary
import com.example.mymoney.ui.viewmodel.AuthMode
import com.example.mymoney.ui.viewmodel.AuthViewModel
import com.example.mymoney.ui.viewmodel.FinanceViewModel

@Composable
fun AuthApp(
    authViewModel: AuthViewModel,
    financeViewModel: FinanceViewModel
) {
    var currentTab by remember { mutableStateOf<AppTab>(AppTab.Overview) }
    val snackbarHostState = remember { SnackbarHostState() }

    val feedback = financeViewModel.feedbackMessage
    LaunchedEffect(feedback) {
        if (!feedback.isNullOrBlank()) {
            snackbarHostState.showSnackbar(feedback)
            financeViewModel.consumeFeedback()
        }
    }

    val isFullScreenOverlay = authViewModel.isAuthOpen ||
        financeViewModel.showAddTransactionSheet ||
        financeViewModel.showPrintPreviewScreen ||
        financeViewModel.showLogoutScreen

    fun selectMainTab(tab: AppTab) {
        financeViewModel.closeAllOverlays()
        currentTab = tab
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        containerColor = VcnSoftGray,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isFullScreenOverlay) {
                FloatingActionButton(
                    onClick = { financeViewModel.openAddTransaction() },
                    shape = CircleShape,
                    containerColor = VcnOrange,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .size(54.dp)
                        .offset(y = 36.dp)
                ) {
                    Icon(
                        imageVector = AppTab.AddTransaction.icon,
                        contentDescription = "Thêm giao dịch"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (!isFullScreenOverlay) {
                VcnBottomBar(
                    currentTab = currentTab,
                    onSelectTab = { tab -> selectMainTab(tab) },
                    onCenterClick = { financeViewModel.openAddTransaction() }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Màn hình 1: Đăng nhập / Đăng ký / Quên mật khẩu
                authViewModel.isAuthOpen -> {
                    when (authViewModel.authMode) {
                        AuthMode.LOGIN -> LoginScreen(
                            message = authViewModel.notice,
                            isLoading = authViewModel.isLoading,
                            onLogin = { email, password ->
                                authViewModel.login(email, password) {
                                    currentTab = AppTab.Overview
                                }
                            },
                            onRegister = { authViewModel.switchAuthMode(AuthMode.REGISTER) },
                            onForgotPassword = { authViewModel.switchAuthMode(AuthMode.FORGOT_PASSWORD) },
                            onResendVerification = { email, password ->
                                authViewModel.resendVerification(email, password)
                            },
                            onContinueAsGuest = { authViewModel.closeAuth() }
                        )

                        AuthMode.REGISTER -> RegisterScreen(
                            message = authViewModel.notice,
                            isLoading = authViewModel.isLoading,
                            onRegister = { email, password ->
                                authViewModel.register(email, password)
                            },
                            onBack = { authViewModel.switchAuthMode(AuthMode.LOGIN) }
                        )

                        AuthMode.FORGOT_PASSWORD -> ForgotPasswordScreen(
                            message = authViewModel.notice,
                            isLoading = authViewModel.isLoading,
                            onReset = { email ->
                                authViewModel.resetPassword(email)
                            },
                            onBack = { authViewModel.switchAuthMode(AuthMode.LOGIN) }
                        )
                    }
                }

                // Màn hình 4: Nhập chi tiêu nhanh (Full screen)
                financeViewModel.showAddTransactionSheet -> {
                    AddTransactionSheet(
                        financeViewModel = financeViewModel,
                        onDismiss = { financeViewModel.closeAddTransaction() },
                        onSave = { title, amount, isExpense, category, walletName, note, dateGroup ->
                            financeViewModel.addTransaction(
                                title = title,
                                amount = amount,
                                isExpense = isExpense,
                                category = category,
                                walletName = walletName,
                                note = note,
                                dateGroup = dateGroup
                            )
                        }
                    )
                }

                // Màn hình 7: Xem trước bản in A4 (Full screen)
                financeViewModel.showPrintPreviewScreen -> {
                    ReportDetailDialog(
                        userEmail = authViewModel.userProfile?.email,
                        financeViewModel = financeViewModel,
                        onDismiss = { financeViewModel.closePrintPreview() }
                    )
                }

                // Màn hình 10: Đăng xuất (Full screen)
                financeViewModel.showLogoutScreen -> {
                    LogoutConfirmDialog(
                        userDisplayName = authViewModel.userProfile?.displayName,
                        userEmail = authViewModel.userProfile?.email,
                        onConfirmLogout = {
                            financeViewModel.closeLogoutConfirm()
                            authViewModel.logout {
                                financeViewModel.showFeedback("Đã đăng xuất khỏi tài khoản VCN")
                                authViewModel.openAuth(AuthMode.LOGIN)
                            }
                        },
                        onDismiss = { financeViewModel.closeLogoutConfirm() }
                    )
                }

                // Màn hình 9: Kế hoạch tháng sau
                financeViewModel.showNextMonthPlanScreen -> {
                    NextMonthPlanScreen(
                        financeViewModel = financeViewModel,
                        onBack = { financeViewModel.closeNextMonthPlan() }
                    )
                }

                // Màn hình 8: Báo cáo chi tiêu
                financeViewModel.showReportsScreen -> {
                    ReportsScreen(
                        financeViewModel = financeViewModel,
                        onBack = { financeViewModel.closeReports() },
                        onOpenPrintPreview = { financeViewModel.openPrintPreview() },
                        onOpenNextMonthPlan = { financeViewModel.openNextMonthPlan() }
                    )
                }

                // 4 Tab chính (Màn 2, 3, 5, 6)
                else -> {
                    when (currentTab) {
                        AppTab.Overview -> OverviewScreen(
                            userDisplayName = authViewModel.userProfile?.displayName,
                            userEmail = authViewModel.userProfile?.email,
                            financeViewModel = financeViewModel,
                            onNavigateToTransactions = { currentTab = AppTab.Transactions },
                            onNavigateToReports = { financeViewModel.openReports() }
                        )

                        AppTab.Transactions -> TransactionsScreen(
                            financeViewModel = financeViewModel
                        )

                        AppTab.Budget -> BudgetScreen(
                            financeViewModel = financeViewModel,
                            onOpenNextMonthPlan = { financeViewModel.openNextMonthPlan() },
                            onOpenReports = { financeViewModel.openReports() }
                        )

                        AppTab.Profile -> SettingsScreen(
                            userProfile = authViewModel.userProfile,
                            financeViewModel = financeViewModel,
                            onOpenAuth = { authViewModel.openAuth(AuthMode.LOGIN) },
                            onOpenReports = { financeViewModel.openReports() },
                            onOpenPrintPreview = { financeViewModel.openPrintPreview() },
                            onOpenNextMonthPlan = { financeViewModel.openNextMonthPlan() },
                            onOpenLogout = { financeViewModel.openLogoutConfirm() }
                        )

                        else -> OverviewScreen(
                            userDisplayName = authViewModel.userProfile?.displayName,
                            userEmail = authViewModel.userProfile?.email,
                            financeViewModel = financeViewModel,
                            onNavigateToTransactions = { currentTab = AppTab.Transactions },
                            onNavigateToReports = { financeViewModel.openReports() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VcnBottomBar(
    currentTab: AppTab,
    onSelectTab: (AppTab) -> Unit,
    onCenterClick: () -> Unit
) {
    Surface(
        color = VcnCardSurface,
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomTabItem(
                    tab = AppTab.Overview,
                    selected = currentTab == AppTab.Overview,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(AppTab.Overview) }
                )
                BottomTabItem(
                    tab = AppTab.Transactions,
                    selected = currentTab == AppTab.Transactions,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(AppTab.Transactions) }
                )
                // Khoảng trống cho nút FAB giữa
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxHeight()
                        .clickable { onCenterClick() }
                )
                BottomTabItem(
                    tab = AppTab.Budget,
                    selected = currentTab == AppTab.Budget,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(AppTab.Budget) }
                )
                BottomTabItem(
                    tab = AppTab.Profile,
                    selected = currentTab == AppTab.Profile,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(AppTab.Profile) }
                )
            }
        }
    }
}

@Composable
private fun BottomTabItem(
    tab: AppTab,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val tint = if (selected) VcnOrange else VcnTextSecondary
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.title,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = tab.title,
            color = tint,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}

