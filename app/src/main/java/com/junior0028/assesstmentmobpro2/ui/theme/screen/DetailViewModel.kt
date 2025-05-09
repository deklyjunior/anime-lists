package com.junior0028.assesstmentmobpro2.ui.theme.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junior0028.assesstmentmobpro2.database.AnimeDao
import com.junior0028.assesstmentmobpro2.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val animeDao: AnimeDao) : ViewModel() {

    private val _animeState = MutableStateFlow<Anime?>(null)
    val animeState: StateFlow<Anime?> = _animeState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getAnimeById(animeId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _animeState.value = animeDao.getAnimeById(animeId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateWatchStatus(watched: Boolean) {
        val currentAnime = _animeState.value ?: return
        val updatedAnime = currentAnime.copy(isWatched = watched)
        viewModelScope.launch {
            try {
                animeDao.update(updatedAnime)
                _animeState.value = updatedAnime
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateAnime(anime: Anime, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                animeDao.update(anime)
                _animeState.value = anime
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun insertAnime(anime: Anime, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                animeDao.insert(anime)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteAnime(onSuccess: () -> Unit) {
        val anime = _animeState.value ?: return
        viewModelScope.launch {
            try {
                animeDao.moveToRecycleBin(anime.id, System.currentTimeMillis())
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}