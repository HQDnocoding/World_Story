package com.example.worldstory.duc.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducviewmodel.DucChapterMarkViewModel

class DucChapterMarkViewModelFactory (private var context :Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DucChapterMarkViewModel::class.java))
        {
            var dbHepler= DatabaseHelper.getInstance(context)
            var repository= DucDataRepository(dbHepler)
            return DucChapterMarkViewModel(repository,context) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}