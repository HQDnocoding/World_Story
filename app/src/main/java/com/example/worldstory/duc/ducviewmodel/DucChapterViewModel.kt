package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducdataclass.DucChapterDataClass
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducutils.getLoremIpsum
import com.example.worldstory.model.Story

class DucChapterViewModel (var context: Context): ViewModel() {
    var isFirstLoadChapter: Boolean = true
    var mainChapter: DucChapterDataClass? = null
    var nextChapter: DucChapterDataClass? = null
    var previousChapter: DucChapterDataClass? = null
    fun setPreMainNextChapter(mChapter: DucChapterDataClass?, pChapter: DucChapterDataClass?,
                              nChapter: DucChapterDataClass?){
        mainChapter=mChapter
        previousChapter=pChapter
        nextChapter=nChapter
    }
    fun getAllChaptersByStory(story: Story): List<DucChapterDataClass> {
        return SampleDataStory.getListOfChapter(context).filter { it.idStory==story.storyID }
    }
    fun getAllChaptersByStory(idStory: Int): List<DucChapterDataClass> {
        return SampleDataStory.getListOfChapter(context).filter { it.idStory==idStory }
    }
    fun getOneChapter(story: DucStoryDataClass, idxChapter: Int): DucChapterDataClass {
        return SampleDataStory.getListOfChapter(context).get(idxChapter)

    }
    fun getOneExampleChapter(): DucChapterDataClass {
        return DucChapterDataClass(1,1, getLoremIpsum(context), SampleDataStory.date)

    }
    fun getNextChapterByCurrentChapter(currentChapter: DucChapterDataClass?): DucChapterDataClass? {
        var tempChapter=getOneExampleChapter()
        var listAllChaptersByStory=getAllChaptersByStory(currentChapter?.idStory?:tempChapter.idStory)
        var idxCurrentChapter = listAllChaptersByStory.indexOf(currentChapter)
        if (listAllChaptersByStory.size > idxCurrentChapter + 1) {
            return listAllChaptersByStory.get(idxCurrentChapter + 1)
        } else {
            return null
        }
    }

    fun getPreviousChapterByCurrentChapter(currentChapter: DucChapterDataClass?): DucChapterDataClass? {
        var tempChapter=getOneExampleChapter()
        var listAllChaptersByStory=getAllChaptersByStory(currentChapter?.idStory?:tempChapter.idStory)

        var idxCurrentChapter =listAllChaptersByStory.indexOf(currentChapter)
        if (0 <= idxCurrentChapter - 1) {
            return listAllChaptersByStory.get(idxCurrentChapter - 1)
        } else {
            return null
        }
    }
}