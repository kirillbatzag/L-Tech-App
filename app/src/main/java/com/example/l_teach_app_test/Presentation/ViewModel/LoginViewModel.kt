package com.example.l_teach_app_test.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.l_teach_app_test.Domain.UseCase.GetPhoneMaskUseCase
import com.example.l_teach_app_test.Domain.UseCase.GetSavedCredentialsUseCase
import com.example.l_teach_app_test.Domain.UseCase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val getSavedCredentialsUseCase: GetSavedCredentialsUseCase,
    private val loginUseCase: LoginUseCase,
    private val getPhoneMaskUseCase: GetPhoneMaskUseCase
) : ViewModel() {

    private val _mask = MutableStateFlow("")
    val mask: StateFlow<String> = _mask.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()


    fun loadPhoneMask() {
        viewModelScope.launch {
            try {
                _mask.value = getPhoneMaskUseCase()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки маски телефона"
            }
        }
    }


    fun login(phone: String, password: String) {
        clearError()

        viewModelScope.launch {
            try {
                val success = loginUseCase(phone, password)
                _loginSuccess.value = success

                if (!success) {
                    _authError.value = "Неверный номер или пароль auth"
                }

            } catch (e: HttpException) {
                when (e.code()) {
                    401, 403 -> {
                        _authError.value = "Неверный пароль"
                    }
                    else -> {
                        _errorMessage.value = "Ошибка сервера: ${e.code()}"
                    }
                }
            } catch (e: IOException) {
                _errorMessage.value = "Проблемы с интернет-соединением"
            } catch (e: Exception) {
                _errorMessage.value = "Произошла ошибка: ${e.localizedMessage}"
            }
        }
    }

    fun getSavedCredentials(): Pair<String?, String?> = getSavedCredentialsUseCase()

    fun clearError() {
        _errorMessage.value = null
        _authError.value = null
    }
    fun clearLoginSuccess() {
        _loginSuccess.value = false
    }
}
