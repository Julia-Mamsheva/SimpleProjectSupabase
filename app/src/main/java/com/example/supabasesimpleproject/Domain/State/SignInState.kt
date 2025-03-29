package com.example.supabasesimpleproject.Domain.State


/**
 * data class (класс данных) SignInState, представляющий состояние формы входа в систем
 * */

data class SignInState (
    val email: String = "test@test.ru",
    val password: String = "123",
    var errorEmail:Boolean = true,
    val errorPassword:Boolean = false
)

/**
 * Используется в архитектуре MVVM (Model-View-ViewModel). Он представляет модель данных для формы входа.
 * ViewModel отслеживает состояние SignInState.  Когда пользователь вводит данные, ViewModel обновляет соответствующие поля в SignInState.
 * Если возникают ошибки (например, неправильный формат email или пустой пароль),  ViewModel обновляет поля errorEmail и errorPassword,
 * что позволяет View (UI) отобразить соответствующие сообщения об ошибках
 * */

