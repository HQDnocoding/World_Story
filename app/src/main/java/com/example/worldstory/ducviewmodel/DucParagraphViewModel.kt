package com.example.worldstory.ducviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worldstory.SampleDataStory
import com.example.worldstory.ducdataclass.DucChapterDataClass
import com.example.worldstory.ducdataclass.DucParagraphDataClass

class DucParagraphViewModel(var context: Context): ViewModel() {

    fun getOneParagraphByChapter(chapter: DucChapterDataClass, pos:Int): DucParagraphDataClass?{
        var paragraph= SampleDataStory.getListOfParagraph(context).filter{ it.idChapter==chapter.idChapter  }
            .filter { it.position==pos  }.get(0)?:null
        return paragraph
    }
    fun getAllParagraphsByChapter(chapter: DucChapterDataClass?): List<DucParagraphDataClass>{
        var clone=chapter?: SampleDataStory.getOneChapter()
        var paragraphs= SampleDataStory.getListOfParagraph(context).filter { it.idChapter==clone.idChapter }
        return paragraphs
    }
    fun getNextParagraphsByChapter(currentParagraph: DucParagraphDataClass): DucParagraphDataClass?{
        var paragraph= SampleDataStory.getListOfParagraph(context).firstOrNull{
            it.position==currentParagraph.position+1
        }
        return paragraph
    }
}