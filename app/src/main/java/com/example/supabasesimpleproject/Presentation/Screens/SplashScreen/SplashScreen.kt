package com.example.supabasesimpleproject.Presentation.Screens.SplashScreen

import android.annotation.SuppressLint
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.supabasesimpleproject.Presentation.Navigation.NavigationRoutes
import com.example.supabasesimpleproject.R
import kotlinx.coroutines.delay

/**
 * Экран заставки (splash screen) с анимированным логотипом перед переходом на экран входа в систему
 * */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SplashScreen(navController: NavHostController) {
    val scale = remember {
        Animatable(0.5f)
    } //объект, используемый для управления масштабированием логотипа

    //Запускает асинхронный блок кода, который выполняется один раз при композиции
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = {
                    OvershootInterpolator(0.5f).getInterpolation(it)
                })
        )// Анимирует изменение значения scale от 0.5f до 2f в течение 1500 миллисекунд, используя OvershootInterpolator для эффекта перескока

        delay(1500L) //Пауза на 1.5 секунды после завершения анимации

        //После паузы, переходит на экран входа в систему (SIGNIN), удаляя экран заставки из стека навигации
        navController.navigate(NavigationRoutes.SIGNIN) {
            popUpTo(NavigationRoutes.SPLASH) {
                inclusive = true
            }
        }
    }

    //Получает размеры экрана
    BoxWithConstraints {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Logo",
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}
