package com.example.mymoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mymoney.ui.theme.AndroidzTheme
import com.example.mymoney.ui.viewmodel.AuthViewModel
import com.example.mymoney.ui.viewmodel.FinanceViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val financeViewModel: FinanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            AndroidzTheme {
                Surface {
                    AuthApp(
                        authViewModel = authViewModel,
                        financeViewModel = financeViewModel
                    )
                }
            }
        }
    }
}
