package com.example.l_teach_app_test.Domain.UseCase

import com.example.l_teach_app_test.Domain.Repository.AuthRepository

class GetSavedCredentialsUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Pair<String?, String?> = repository.getSavedCredentials()
}
