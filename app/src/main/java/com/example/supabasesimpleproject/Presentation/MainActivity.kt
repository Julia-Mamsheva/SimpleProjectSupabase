package com.example.supabasesimpleproject.Presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.supabasesimpleproject.Presentation.Navigation.NavHost
import com.example.supabasesimpleproject.Presentation.ui.theme.TestForLectionTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    @RequiresApi(Build.VERSION_CODES.S)
    // Стандартный метод жизненного цикла Activity, который вызывается при создании Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        // Вызывает метод onCreate родительского класса
        super.onCreate(savedInstanceState)
        // Пользовательская функция (не показана в коде), которая, настраивает отображение контента от края до края экрана,
        // используя возможности Android версии S и выше
        enableEdgeToEdge()
        setContent {
            // Устанавливает тему приложения, определяя цвета, стили и т.д
            TestForLectionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    // Функция, которая, создает NavHost для навигации между различными экранами приложения
                    NavHost()
                }
            }
        }
    }
}

