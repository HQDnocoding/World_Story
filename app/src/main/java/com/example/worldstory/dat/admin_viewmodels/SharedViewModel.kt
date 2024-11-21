package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val add = MutableLiveData<Boolean?>()
    private val search = MutableLiveData<Boolean?>()
    private val filterBtn = MutableLiveData<Boolean?>()
    val recyclerViewStateStory = MutableLiveData<RecyclerViewState>()
    val recycleViewStateUser=MutableLiveData<RecyclerViewState>()
    val searchQueryStory = MutableLiveData<String>()
    private val selectedChips = mutableSetOf<Int>()
    val _add: LiveData<Boolean?> get() = add
    val _search: LiveData<Boolean?> get() = search
    val _filterBtn: LiveData<Boolean?> get() = filterBtn
    val _selectedChips: MutableSet<Int> get() = selectedChips

    //////////
    fun addSeclectedChip(text: Int) {
        selectedChips.add(text)
    }

    fun delSelectedChip(text: Int) {
        selectedChips.remove(text)
    }

    fun isContainChip(text: Int): Boolean {
        return selectedChips.contains(text)
    }


    //
    //
    fun onFilterBtnClicked() {
        filterBtn.value = true
    }

    fun filterBtnHandled() {
        filterBtn.value = null
    }

    //
    //
    fun onAddButtonClicked() {
        add.value = true
    }

    fun addHandled() {
        add.value = null
    }

    //
    //
    fun onSearch() {
        search.value = true
    }

    fun searchHandle() {
        search.value = null
    }
    /////////
}

data class RecyclerViewState(val firstVisibleItemPosition: Int, val offset: Int)
