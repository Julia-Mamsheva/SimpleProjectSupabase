package com.example.supabasesimpleproject.Presentation.Screens.SignUpScreen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supabasesimpleproject.Domain.State.ResultState
import com.example.supabasesimpleproject.Presentation.Navigation.NavigationRoutes
import com.example.supabasesimpleproject.Presentation.Screens.Components.ButtonNavigation
import com.example.supabasesimpleproject.Presentation.Screens.Components.TextFieldEmail
import com.example.supabasesimpleproject.Presentation.Screens.Components.TextFieldPassword
import com.example.supabasesimpleproject.Presentation.Screens.Components.TextFieldStandart
import java.util.Calendar
import java.util.Date

/**
 * composable-функция SignUpScreen для экрана регистрации пользователя
 * */

@Composable
// navController: NavHostController:  Объект для навигации между экранами.
// sigUpViewModel: SignUpViewModel = viewModel() :  ViewModel, управляющий состоянием и логикой экрана входа.
fun SignUpScreen(navController: NavHostController, sigUpViewModel: SignUpViewModel = viewModel()) {

    val uiState = sigUpViewModel.uiState // Получение текущего состояния UI из ViewMode

    val mContext = LocalContext.current // Получение контекста Android

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("Выберите дату") }

    val resultState by sigUpViewModel.resultState.collectAsState() //Собирает значения из StateFlow в ViewModel, используя collectAsState() для синхронного доступа в Composable

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {

        TextFieldEmail(uiState.email,uiState.isEmailError) { sigUpViewModel.updateState(uiState.copy(email = it)) }
        Spacer(Modifier.height(10.dp))

        TextFieldStandart(uiState.username) { sigUpViewModel.updateState(uiState.copy(username = it)) }
        Spacer(Modifier.height(10.dp))

        TextFieldStandart(uiState.surname) { sigUpViewModel.updateState(uiState.copy(surname = it)) }
        Spacer(Modifier.height(10.dp))

        TextFieldPassword(uiState.password) { sigUpViewModel.updateState(uiState.copy(password = it)) }
        Spacer(Modifier.height(10.dp))

        TextFieldPassword(uiState.confirmPassword) { sigUpViewModel.updateState(uiState.copy(confirmPassword = it)) }
        Spacer(Modifier.height(10.dp))

        val mDatePickerDialog = DatePickerDialog(
            mContext,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mYear-${mMonth+1}-$mDayOfMonth"
                sigUpViewModel.updateState(uiState.copy(dateBirth = "$mYear-${mMonth+1}-$mDayOfMonth"))
            }, mYear, mMonth, mDay
        )

        Text(
            text = mDate.value,
            modifier = Modifier.clickable {
                mDatePickerDialog.show()
            }
        )
        Spacer(Modifier.height(10.dp))
        when (resultState) {
            is ResultState.Error -> {
                ButtonNavigation("Зарегистрироваться") { sigUpViewModel.signUp() }
                Text((resultState as ResultState.Error).message)
            }
            is ResultState.Initialized -> {
                ButtonNavigation("Зарегистрироваться") { sigUpViewModel.signUp() }
            }
            ResultState.Loading -> {
                CircularProgressIndicator()
            }
            is ResultState.Success -> {
                navController.navigate(NavigationRoutes.MAIN)
                {
                    popUpTo(NavigationRoutes.SIGNIN) {
                        inclusive = true
                    }
                }
            }
        }
    }
}
/**
 * SignUpViewModel эффективно управляет логикой регистрации пользователя, используя MVVM и асинхронные операции с помощью корутин
 * */