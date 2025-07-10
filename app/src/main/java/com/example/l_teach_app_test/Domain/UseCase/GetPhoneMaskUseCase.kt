package com.example.l_teach_app_test.Domain.UseCase

import com.example.l_teach_app_test.Domain.Repository.AuthRepository

class GetPhoneMaskUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): String = repository.getPhoneMask()
}