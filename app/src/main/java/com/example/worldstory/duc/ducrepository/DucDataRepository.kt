package com.example.worldstory.duc.ducrepository

import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Rate
import com.example.worldstory.model.Story
import com.example.worldstory.model.User

class DucDataRepository(private var dbHelper: DatabaseHelper) {
    //story
    fun getAllStories(): List<Story> {
        var list= dbHelper.getAllStories()
        return list
    }
   // fun getStoryById(id: Int): Story? = dbHelper.getStoryById(id)
    fun addStory(story: Story): Long = dbHelper.insertStory(story)

    fun getStoriesByGenre(genreId: Int,isText: Boolean): List<Story>{
        var setOfStoryId=dbHelper.getStoriesIdbyGenreId(genreId)
        var tempStory= SampleDataStory.getexampleStory()
        var listOfStories=mutableListOf<Story>()
        setOfStoryId.forEach{
            var story=dbHelper.getStoryByStoryId(it)?:tempStory
            if( story.isTextStory.toBoolean()==isText){
                listOfStories.add(story)
            }

        }
        return listOfStories

    }

    //genre
    fun getAllGenres(): List<Genre>{
        var list =dbHelper.getAllGenres()
        return list
    }
    fun getGenresByStory(storyId: Int): List<Genre>{
        var setOfGenresId=dbHelper.getGenreIDbyStoryID(storyId)
        var tempGenre= SampleDataStory.getexampleGenre()
        var listOfGenres=mutableListOf<Genre>()
        setOfGenresId.forEach{
            listOfGenres.add(dbHelper.getGenreByGenresId(it)?:tempGenre)
        }
        return listOfGenres
    }
    //chapter
    fun getAllChapter(): List<Chapter>{
        var list=dbHelper.getAllChapters()
        return list
    }
    fun getChaptersByStory(storyId: Int): List<Chapter>{
        var list=dbHelper.getChaptersByStory(storyId)
        return list
    }

    //paragraph
    fun getAllParagraph(): List<Paragraph>{
        var list=dbHelper.getAllParagraphs()
        return list
    }
    fun getParagraphsByChapter(chapterId:Int): List<Paragraph>{
        return dbHelper.getParagraphsByChapter(chapterId)
    }


    //image
    fun getImagesByChapter(chapterId: Int): List<Image>{
        return dbHelper.getImagesByChapter(chapterId)
    }

    //comment

    fun getCommentsByStory(storyId:Int):List<Comment>{
        return dbHelper.getCommentsByStory(storyId)
    }
    //user
     fun createGuestUser(){
         dbHelper.insertUser(User(null,"guest","", SampleDataStory.getExampleImgURL(),"Guest",1,
             dateTimeNow
         ))
     }

    //rating
    fun getRatingsByStory(storyId: Int): List<Rate>{
        return dbHelper.getRatesByStory(storyId)
    }
    fun ratingStoryByCurrentUser(rate: Rate){

        //xoa cai cu
        dbHelper.deleteRate(rate)
        // them cai moi
        dbHelper.insertRate(rate)
    }

}