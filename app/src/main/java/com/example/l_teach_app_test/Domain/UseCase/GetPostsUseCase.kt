package com.example.l_teach_app_test.Domain.UseCase

import com.example.l_teach_app_test.Domain.Model.Post
import com.example.l_teach_app_test.Domain.Repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPostsUseCase(private val postRepository: PostRepository)  {
    suspend operator fun invoke(): Result<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(postRepository.getPosts())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}