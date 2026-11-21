
package com.example.mymoney.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    // 1. Đăng ký tài khoản
    fun register(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = task.result?.user

                    if (user == null) {
                        onResult(false, "Không thể tạo tài khoản.")
                        return@addOnCompleteListener
                    }

                    // Gửi email xác minh
                    user.sendEmailVerification()
                        .addOnCompleteListener { verifyTask ->

                            auth.signOut()

                            if (verifyTask.isSuccessful) {
                                onResult(
                                    true,
                                    "Đăng ký thành công! Hãy kiểm tra email để xác minh tài khoản."
                                )
                            } else {
                                onResult(
                                    true,
                                    "Tài khoản đã được tạo nhưng chưa gửi được email xác minh. Hãy thử gửi lại."
                                )
                            }
                        }

                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Đăng ký thất bại."
                    )
                }
            }
    }
    fun resendVerification(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = auth.currentUser

                    if (user == null) {
                        onResult(false, "Không tìm thấy tài khoản.")
                        return@addOnCompleteListener
                    }

                    if (user.isEmailVerified) {
                        auth.signOut()
                        onResult(true, "Email đã được xác minh.")
                        return@addOnCompleteListener
                    }

                    user.sendEmailVerification()
                        .addOnCompleteListener { verifyTask ->

                            auth.signOut()

                            if (verifyTask.isSuccessful) {
                                onResult(
                                    true,
                                    "Đã gửi lại email xác minh."
                                )
                            } else {
                                onResult(
                                    false,
                                    verifyTask.exception?.localizedMessage
                                        ?: "Không gửi được email."
                                )
                            }
                        }

                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Không thể xác thực tài khoản."
                    )
                }
            }
    }
    // 2. Đăng nhập
    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = task.result?.user

                    if (user == null) {
                        auth.signOut()
                        onResult(false, "Không tìm thấy tài khoản.")
                        return@addOnCompleteListener
                    }

                    // Tải lại thông tin tài khoản
                    user.reload().addOnCompleteListener { reloadTask ->

                        if (!reloadTask.isSuccessful) {
                            auth.signOut()
                            onResult(false, "Không thể kiểm tra tài khoản.")
                            return@addOnCompleteListener
                        }

                        onResult(true, "Đăng nhập thành công!")
                    }

                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Đăng nhập thất bại."
                    )
                }
            }
    }

    // 3. Quên mật khẩu
    fun resetPassword(
        email: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(
                        true,
                        "Đã gửi email đặt lại mật khẩu thành công!"
                    )
                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Không thể gửi email."
                    )
                }
            }
    }

    // Xác nhận mã và đổi mật khẩu mới
    fun confirmPasswordReset(
        code: String,
        newPassword: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (code.isBlank()) {
            onResult(false, "Vui lòng nhập mã xác minh từ link email.")
            return
        }
        auth.confirmPasswordReset(code, newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Đổi mật khẩu thành công! Bạn có thể đăng nhập bằng mật khẩu mới.")
                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Mã xác minh không đúng hoặc đã hết hạn."
                    )
                }
            }
    }

    // 4. Đăng xuất
    fun logout() {
        auth.signOut()
    }

    // Lấy email người dùng hiện tại
    fun getCurrentEmail(): String? {
        return auth.currentUser?.email
    }

    // Lấy thông tin UserProfile (OOP)
    fun getUserProfile(): com.example.mymoney.data.model.UserProfile? {
        val user = auth.currentUser ?: return null
        return com.example.mymoney.data.model.UserProfile(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        )
    }

    // Kiểm tra đăng nhập
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
