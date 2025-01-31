package com.example.savemysoul2_0.ui.screens.AddUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savemysoul2_0.data.model.TelegramUser
import com.example.savemysoul2_0.data.repo.TelegramUserRepoImple
import com.example.savemysoul2_0.domain.useCase.AddScreenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddUserViewModel: ViewModel() {
    private val _useCase = AddScreenUseCase(TelegramUserRepoImple())
    private val _uiState = MutableStateFlow(AddUserUiState())
    private val _telegramUser = MutableStateFlow(TelegramUser())
    val uiState: Flow<AddUserUiState> get() = _uiState
    val telegramUser: Flow<TelegramUser> get() = _telegramUser

    fun setToast(toast: String) {
        _uiState.value = _uiState.value.copy(toast = toast)
    }

    fun setIsSuccess(isSuccess: Boolean) {
        _uiState.value = _uiState.value.copy(isSuccess = isSuccess)
    }

    fun onTelegramIdChanged(id: String) {
        _telegramUser.value = _telegramUser.value.copy(telegramId = id)
    }

    fun onTelegramMessageChanged(message: String) {
        _telegramUser.value = _telegramUser.value.copy(telegramMessage = message)
    }

    fun addTelegramUser(uuid: String) {
        val currentTelegramId = _telegramUser.value.telegramId
        val currentTelegramMessage = _telegramUser.value.telegramMessage

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (currentTelegramId.isNotBlank() && currentTelegramMessage.isNotEmpty()) {
                    val response = _useCase.addTelegramUser(TelegramUser(identifier = uuid, telegramId = currentTelegramId, telegramMessage = currentTelegramMessage))
                    setToast(response)
                    setIsSuccess(true)
                } else {
                    withContext(Dispatchers.Main) {
                        setToast("Заповніть всі поля")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    setToast("Помилка: $e")
                }
            }
        }
    }
}