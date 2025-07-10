package com.example.l_teach_app_test.Domain.UseCase

import com.example.l_teach_app_test.Domain.Repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(phone: String, password: String): Boolean =
        repository.login(phone, password)
}
