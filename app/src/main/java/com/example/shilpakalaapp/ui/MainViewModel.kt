package com.example.shilpakalaapp.ui

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shilpakalaapp.data.*
import com.example.shilpakalaapp.utils.ImageUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ImageRepository) : ViewModel() {

    var currentUser by mutableStateOf<UserEntity?>(null)
        private set

    var isProcessing by mutableStateOf(false)
        private set

    val imagesWithUser: StateFlow<List<ImageWithUser>> = repository.getImagesWithUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun registerUser(user: UserEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val id = repository.insertUser(user)
            currentUser = user.copy(id = id)
            onSuccess()
        }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmailAndPassword(email, password)
            if (user != null) {
                currentUser = user
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        currentUser = null
    }

    fun enhanceAndUpload(
        context: Context,
        uri: Uri,
        productName: String,
        woodType: String,
        price: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = currentUser?.id ?: return
        viewModelScope.launch {
            isProcessing = true
            try {
                val bitmap = ImageUtils.uriToBitmap(context, uri)
                if (bitmap != null) {
                    val processed = ImageUtils.processImage(bitmap, productName, woodType, price)
                    val finalUri = ImageUtils.saveBitmapToFile(context, processed)
                    if (finalUri != null) {
                        val imageEntity = ImageEntity(
                            uri = finalUri.toString(),
                            userId = userId,
                            productName = productName,
                            woodType = woodType,
                            price = price
                        )
                        repository.insertImage(imageEntity)
                        onSuccess()
                    } else throw Exception("Failed to save image")
                } else throw Exception("Could not load image")
            } catch (e: Exception) {
                onError(e.message ?: "Processing error")
            } finally {
                isProcessing = false
            }
        }
    }

    fun updateRating(image: ImageEntity, rating: Float) {
        viewModelScope.launch {
            repository.updateImage(image.copy(rating = rating))
        }
    }
}

class MainViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
