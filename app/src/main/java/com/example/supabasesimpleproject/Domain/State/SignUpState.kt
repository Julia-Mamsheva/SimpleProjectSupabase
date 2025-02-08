package com.example.supabasesimpleproject.Domain.State


/**
 * data class SignUpState, представляющий состояние формы регистрации пользователя
 * */

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "",
    val surname:String = "",
    val dateBirth: String? = null,
    var isEmailError:Boolean = false
)
/**
 * Используется в архитектуре MVVM (Model-View-ViewModel).  SignUpState представляет модель данных для формы регистрации
 * ViewModel отслеживает состояние SignUpState и обновляет его по мере ввода пользователем данных в форму.
 * Если возникают ошибки (например, неправильный формат email, пароли не совпадают, пустые поля),
 * ViewModel обновляет соответствующие поля (в этом случае, только isEmailError).
 * View (UI) отображает состояние SignUpState, показывая введенные данные и сообщения об ошибках
 * */

