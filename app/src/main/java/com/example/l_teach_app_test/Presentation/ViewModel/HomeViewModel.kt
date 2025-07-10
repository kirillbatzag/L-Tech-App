package com.example.l_teach_app_test.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.l_teach_app_test.Domain.Model.Post
import com.example.l_teach_app_test.Domain.UseCase.GetPostsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


enum class SortOrder {
    BY_SERVER, 
    BY_DATE    
}

class HomeViewModel(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _currentSortOrder = MutableStateFlow(SortOrder.BY_SERVER)
    val currentSortOrder: StateFlow<SortOrder> = _currentSortOrder.asStateFlow()
    
    private var originalPosts: List<Post> = emptyList()

    init {
        loadPosts()
    }

    fun loadPosts(isRefresh: Boolean = false) {
        if (_isLoading.value == true && !isRefresh) {
            return 
        }
        _isLoading.value = true
        _errorMessage.value = null 

        viewModelScope.launch {
            val result = getPostsUseCase()
            _isLoading.value = false 

            result.onSuccess { loadedPosts ->
                originalPosts = loadedPosts 
                applySortOrder(_currentSortOrder.value) 
            }.onFailure { e ->
                _errorMessage.value = e.message ?: "Неизвестная ошибка при загрузке постов"
            }
        }
    }
    
    fun setSortOrder(sortOrder: SortOrder) {
        if (_currentSortOrder.value != sortOrder) {
            _currentSortOrder.value = sortOrder
            applySortOrder(sortOrder)
        } 
    }


    
    private fun applySortOrder(sortOrder: SortOrder) {
        val sortedList = when (sortOrder) {
            SortOrder.BY_SERVER -> originalPosts.sortedBy { it.sortOrder } 
            SortOrder.BY_DATE -> originalPosts.sortedByDescending { it.date } 
        }
        _posts.value = sortedList 
    }
    
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}