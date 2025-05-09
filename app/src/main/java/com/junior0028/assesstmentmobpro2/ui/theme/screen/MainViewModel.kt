package com.junior0028.assesstmentmobpro2.ui.theme.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junior0028.assesstmentmobpro2.database.AnimeDao
import com.junior0028.assesstmentmobpro2.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val animeDao: AnimeDao) : ViewModel() {
    val activeAnimeList: Flow<List<Anime>> = animeDao.getAnime()
    val recycleBinAnimeList: Flow<List<Anime>> = animeDao.getDeletedAnime()


    fun moveToRecycleBin(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            animeDao.moveToRecycleBin(id, System.currentTimeMillis())
        }
    }

    fun restoreFromRecycleBin(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            animeDao.restoreFromRecycleBin(id)
        }
    }

    fun permanentlyDelete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            animeDao.permanentlyDeleteById(id)
        }
    }

    fun emptyRecycleBin() {
        viewModelScope.launch(Dispatchers.IO) {
            animeDao.emptyRecycleBin()
        }
    }
}