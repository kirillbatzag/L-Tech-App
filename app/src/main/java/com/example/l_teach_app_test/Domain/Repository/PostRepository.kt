package com.example.l_teach_app_test.Domain.Repository

import com.example.l_teach_app_test.Domain.Model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
}