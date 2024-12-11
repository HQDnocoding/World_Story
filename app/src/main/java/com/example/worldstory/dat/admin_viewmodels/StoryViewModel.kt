package com.example.worldstory.dat.admin_viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.toInt
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryViewModel(private val db: DatabaseHelper, private val _type:Int) :
    ViewModel(db) {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories
    val title = MutableLiveData<String>()
    val author = MutableLiveData<String>()
    val decription = MutableLiveData<String>()
    val isText = MutableLiveData(false)
    val storyBgImg = mutableListOf<String>()
    val storyImg = mutableListOf<String>()
    val genreIDList = MutableLiveData<List<Int>>()
    val storyGenreMap = mutableMapOf<Int, Set<Int>>()

    val currentStoryID = MutableLiveData(-1)
    val chapterListByStory = MutableLiveData<List<Chapter>>()

    var type = _type
    private val tempList = mutableListOf<Story>()
    private val tempMap = mutableMapOf<Int, Set<Int>>()

    init {
//        fetchAllStoriesByType(type)
        fetchAllStoriesByType(type)
    }

    fun fetchAllChapters() {
        val tempChapterList = mutableListOf<Chapter>()
        if (currentStoryID.value!! >= 0)
            tempChapterList.addAll(db.getChaptersByStory(currentStoryID.value!!))

        chapterListByStory.value = tempChapterList
    }

    fun fetchAllChaptersAsynce() {
        val tempChapterList = mutableListOf<Chapter>()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentStoryID.value?.let { storyId ->
                    if (storyId >= 0) {
                        tempChapterList.addAll(db.getChaptersByStory(storyId))
                    }
                }
            }

            chapterListByStory.value = tempChapterList
        }
    }

    fun setIDStory(id: Int?) {
        currentStoryID.value = id
        fetchAllChapters()
    }

    fun setStoryByID(id: Int?) {
        try {
            val story = stories.value?.filter { it.storyID == id }!!.first()
            author.value = story.author
            decription.value = story.description
            isText.value = if (story.isTextStory == 1) true else false
            title.value = story.title
            genreIDList.value = storyGenreMap[story.storyID]?.toList()
            storyBgImg.add(story.bgImgUrl)
            storyImg.add(story.imgUrl)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun onAddNewStory(): Long {
        val story = Story(
            null,
            title.value.toString(),
            author.value.toString(),
            decription.value.toString(),
            SampleDataStory.getExampleImgURL(),
            SampleDataStory.getExampleImgURLParagraph(),
            isTextStory = isText.value?.toInt() ?: 1,
            dateTimeNow(),
            Float.MAX_VALUE / Float.MAX_VALUE,
            1
        )

        val l: Long = insertStory(story)
        genreIDList.value?.forEach { gID -> insertStoryGenre(l.toInt(), gID) }
        resetValue()
        fetchAllStoriesByTypeAsynce(type ?: 0)
        return l
    }

    fun resetValue() {
        genreIDList.value = emptyList()
        decription.value = ""
        isText.value = false
        author.value = ""
        title.value = ""
        storyImg.clear()
        storyBgImg.clear()
    }

    fun fetchAllStoriesByTypeAsynce(id: Int) {
        Log.w("asynce", "yes")
        try {
            type = id
            tempMap.clear()
            tempList.clear()
            viewModelScope.launch {

                withContext(Dispatchers.IO) {

                    tempList.addAll(db.getStoriesByType(type ?: 0))
                    tempList.forEach { s ->
                        tempMap[s.storyID ?: 0] = db.getGenreIDbyStoryID(s.storyID)
                    }
                }

                _stories.value = tempList
                storyGenreMap.values.clear()
                tempMap.forEach { k, v -> storyGenreMap[k] = v }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun fetchAllStoriesByType(id: Int ) {
        try {
            type = id
            tempMap.clear()
            tempList.clear()

            tempList.addAll(db.getStoriesByType(type ?: 0))
            tempList.forEach { s ->
                tempMap[s.storyID ?: 0] = db.getGenreIDbyStoryID(s.storyID)
            }


            _stories.value = tempList
            storyGenreMap.values.clear()
            tempMap.forEach { k, v -> storyGenreMap[k] = v }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun insertStory(story: Story): Long {
        val l = db.insertStory(story)
        return l
    }

    fun insertStoryGenre(sID: Int, gID: Int): Long {
        return db.insertStoryGenre(sID, gID)
    }


    fun deleteStory(story: Story): Int {
        val i: Int = db.deleteStory(story.storyID)
        try {
            fetchAllStoriesByTypeAsynce(type?: 0)
            return i
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    fun updateBackGroundStory(story: Story?): Int {
        val s = Story(
            storyID = story?.storyID,
            title = title.value.toString(),
            isTextStory = story?.isTextStory ?: 1,
            createdDate = story?.createdDate ?: "",
            userID = story?.userID ?: -1,
            description = decription.value.toString(),
            score = story?.score ?: 0f,
            author = author.value.toString(),
            imgUrl = story?.imgUrl ?: "",
            bgImgUrl = transform(storyBgImg.first())
        )
        try {
            val i: Int = db.updateBackgroundStory(s)
            fetchAllStoriesByTypeAsynce(type?: 0)
            resetValue()
            return i
        } catch (e: Exception) {
            resetValue()
            e.printStackTrace()
            return -1
        }

    }

    fun updateFaceStory(story: Story?): Int {
        val s = Story(
            storyID = story?.storyID,
            title = title.value.toString(),
            isTextStory = story?.isTextStory ?: 1,
            createdDate = story?.createdDate ?: "",
            userID = story?.userID ?: -1,
            description = decription.value.toString(),
            score = story?.score ?: 0f,
            author = author.value.toString(),
            imgUrl = transform(storyImg.first()),
            bgImgUrl = story?.bgImgUrl ?: ""
        )
        try {
            val i: Int = db.updateFaceStory(s)
            fetchAllStoriesByTypeAsynce(type ?: 0)
            resetValue()
            return i
        } catch (e: Exception) {
            resetValue()
            e.printStackTrace()
            return -1
        }

    }


    fun updateInforStory(story: Story?): Int {
        val s = Story(
            storyID = story?.storyID,
            title = title.value.toString(),
            isTextStory = story?.isTextStory ?: 1,
            createdDate = story?.createdDate ?: "",
            userID = story?.userID ?: -1,
            description = decription.value.toString(),
            score = story?.score ?: 0f,
            author = author.value.toString(),
            imgUrl = story?.imgUrl ?: "",
            bgImgUrl = story?.bgImgUrl ?: ""
        )
        try {
            val i: Int = db.updateInforStory(s)
            fetchAllStoriesByTypeAsynce(type?: 0)
            resetValue()
            return i
        } catch (e: Exception) {
            resetValue()
            e.printStackTrace()
            return -1
        }

    }

    fun updateStory(story: Story?): Int {
        val s = Story(
            storyID = story?.storyID,
            title = title.value.toString(),
            isTextStory = story?.isTextStory ?: 1,
            createdDate = story?.createdDate ?: "",
            userID = story?.userID ?: -1,
            description = decription.value.toString(),
            score = story?.score ?: 0f,
            author = author.value.toString(),
            imgUrl = transform(storyImg.first()),
            bgImgUrl = transform(storyBgImg.first())
        )
        try {
            val i: Int = db.updateStory(s)
            fetchAllStoriesByTypeAsynce(type?: 0)
            resetValue()
            return i
        } catch (e: Exception) {
            resetValue()
            e.printStackTrace()
            return -1
        }

    }

    fun getStoryById(id: Int): Story? {
        try {
            return stories.value?.first { it.storyID == id }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun transform(id: String): String {
        return "https://drive.usercontent.google.com/download?id=${id}&export=view"
    }

    fun deleteChapter(id: Int) {
        db.deleteChapter(id)
        fetchAllChaptersAsynce()
    }
}

class StoryViewModelFactory(private val databaseHelper: DatabaseHelper, private val _type: Int=0) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(databaseHelper,_type) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}