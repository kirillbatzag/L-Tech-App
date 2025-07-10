package com.example.l_teach_app_test.Data.Repository

import android.content.SharedPreferences
import com.example.l_teach_app_test.Data.Remote.ApiService
import com.example.l_teach_app_test.Domain.Repository.AuthRepository

class AuthRepositoryImpl(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
): AuthRepository {

    override suspend fun getPhoneMask(): String {
        return api.getPhoneMask().phoneMask
    }

    override suspend fun login(phone: String, password: String): Boolean {
        val response = api.login(phone, password)
        if (response.success) {
            sharedPreferences.edit()
                .putString("phone", phone)
                .putString("password", password)
                .apply()
        }
        return response.success
    }

    override fun getSavedCredentials(): Pair<String?, String?> {
        return sharedPreferences.getString("phone", null) to
                sharedPreferences.getString("password", null)
    }
}