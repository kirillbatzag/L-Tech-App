package com.example.l_teach_app_test.Data.Repository

import com.example.l_teach_app_test.Data.Remote.ApiService
import com.example.l_teach_app_test.Domain.Model.Post
import com.example.l_teach_app_test.Domain.Repository.PostRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostRepositoryImpl(private val api: ApiService) : PostRepository {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    private val BASE_URL = "http://dev-exam.l-tech.ru"

    override suspend fun getPosts(): List<Post> {
        return api.getPosts().map { dto ->
            Post(
                id = dto.id,
                title = dto.title,
                text = dto.text,
                imageUrl = BASE_URL + dto.image,
                date = dateFormatter.parse(dto.date) ?: Date(0),
                sortOrder = dto.sort
            )
        }
    }
}