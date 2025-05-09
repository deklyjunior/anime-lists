package com.junior0028.assesstmentmobpro2.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.junior0028.assesstmentmobpro2.database.AnimeDb
import com.junior0028.assesstmentmobpro2.ui.theme.screen.DetailViewModel
import com.junior0028.assesstmentmobpro2.ui.theme.screen.MainViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AnimeDb.getInstance(context).dao
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(dao) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(dao) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}