package com.example.mymoney.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mymoney.data.model.UserProfile
import com.example.mymoney.data.repository.AuthRepository

enum class AuthMode {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD
}

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var userProfile by mutableStateOf<UserProfile?>(authRepository.getUserProfile())
        private set

    var notice by mutableStateOf("")
        private set

    var isAuthOpen by mutableStateOf(false)
        private set

    var authMode by mutableStateOf(AuthMode.LOGIN)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun refreshUser() {
        userProfile = authRepository.getUserProfile()
    }

    fun openAuth(mode: AuthMode = AuthMode.LOGIN) {
        notice = ""
        authMode = mode
        isAuthOpen = true
    }

    fun closeAuth() {
        notice = ""
        isAuthOpen = false
    }

    fun switchAuthMode(mode: AuthMode) {
        notice = ""
        authMode = mode
    }

    fun clearNotice() {
        notice = ""
    }

    fun login(email: String, password: String, onSuccess: () -> Unit = {}) {
        isLoading = true
        notice = ""
        authRepository.login(email, password) { success, message ->
            isLoading = false
            notice = message
            if (success) {
                refreshUser()
                isAuthOpen = false
                onSuccess()
            }
        }
    }

    fun register(email: String, password: String) {
        isLoading = true
        notice = ""
        authRepository.register(email, password) { success, message ->
            isLoading = false
            notice = message
            if (success) {
                authMode = AuthMode.LOGIN
            }
        }
    }

    fun resetPassword(email: String) {
        isLoading = true
        notice = ""
        authRepository.resetPassword(email) { _, message ->
            isLoading = false
            notice = message
        }
    }

    fun resendVerification(email: String, password: String) {
        isLoading = true
        notice = "Đang gửi lại email xác minh..."
        authRepository.resendVerification(email, password) { _, message ->
            isLoading = false
            notice = message
        }
    }

    fun logout(onLoggedOut: () -> Unit = {}) {
        authRepository.logout()
        refreshUser()
        notice = "Bạn đã đăng xuất khỏi VCN."
        isAuthOpen = false
        onLoggedOut()
    }
}
