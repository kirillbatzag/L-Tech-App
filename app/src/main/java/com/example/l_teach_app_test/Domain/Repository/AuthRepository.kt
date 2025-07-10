package com.example.l_teach_app_test.Domain.Repository

interface AuthRepository {
    suspend fun getPhoneMask(): String
    suspend fun login(phone: String, password: String): Boolean
    fun getSavedCredentials(): Pair<String?, String?>

}