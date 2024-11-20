package com.example.worldstory.dbhelper

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.worldstory.dbhelper.Contract.CommentEntry
import com.example.worldstory.model.Role
import com.example.worldstory.model.User


object Contract {
    //Story table
    object StoryEntry : BaseColumns {
        const val TABLE_NAME = "story_table"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_BACKGROUND_IMAGE_URL = "background_image_url"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CREATED_DATE = "created_date"
        const val COLUMN_IS_TEXT_STORY = "is_text_story"
        const val COLUMN_SCORE = "score"

        // Foreign key
        //const val COLUMN_GENRE_ID_FK = "genre_id"
        const val COLUMN_USER_CREATED_ID_FK = "user_created_id"

    }

    //Genre table
    object GenreEntry : BaseColumns {
        const val TABLE_NAME = "genre_table"
        const val COLUMN_NAME = "name"

        //Foreign key
        const val COLUMN_USER_CREATED_ID_FK = "user_created_id"
    }

    // story have many genres , a genre have many stories
    object StoryGenreEntry : BaseColumns {
        const val TABLE_NAME = "story_genre_table"

        //Foreign key , 2 key nay khong duoc trung voi 2 key khac
        const val COLUMN_STORY_ID_FK = "story_id"
        const val COLUMN_GENRE_ID_FK = "genre_id"
    }

    //User table
    object UserEntry : BaseColumns {
        const val TABLE_NAME = "user_table"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NICKNAME = "nickname"
        const val COLUMN_CREATED_DATE = "created_date"

        //Foreign key
        const val COLUMN_ROLE_ID_FK = "role_id"
    }

    //Role table
    object RoleEntry : BaseColumns {
        const val TABLE_NAME = "role_table"
        const val COLUMN_NAME = "name"
    }

    //user love story table
    object UserLoveStory : BaseColumns {
        const val TABLE_NAME = "user_love_story_table"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Rate table
    object RateEntry : BaseColumns {
        const val TABLE_NAME = "rate_table"
        const val COLUMN_RATE = "rate"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Comment table
    object CommentEntry : BaseColumns {
        const val TABLE_NAME = "comment_table"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TIME = "time"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Chapter table
    object ChapterEntry : BaseColumns {
        const val TABLE_NAME = "chapter_table"
        const val COLUMN_TITLE = "title"

        //Foreign key
        //const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    // user marked chapter table
    object ChapterMarkEntry : BaseColumns {
        const val TABLE_NAME = "chaptermark_table"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    // user saw chapter
    object ChapterHistoryEntry : BaseColumns {
        const val TABLE_NAME = "chapter_history_table"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    //Paragraph table
    object ParagraphEntry : BaseColumns {
        const val TABLE_NAME = "paragraph_table"
        const val COLUMN_CONTENT_FILE = "text_url"
        const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    //Imange table
    object ImageEntry : BaseColumns {
        const val TABLE_NAME = "img_table"
        const val COLUMN_CONTENT_FILE = "img_url"
        const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    //ReadHistory table
    object ReadHistory : BaseColumns {
        const val TABLE_NAME = "history_read_table"
        const val COLUMN_DATE_TIME = "date_time"

        //Foreign
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }
}

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_doc_truyen_db"
        private const val DATABASE_VERSION = 1
        private const val _ID = BaseColumns._ID

    }

    override fun onCreate(p0: SQLiteDatabase?) {

        var createStoryTable = """
            create table ${Contract.StoryEntry.TABLE_NAME} (
            ${_ID} integer primary key autoincrement,
            ${Contract.StoryEntry.COLUMN_TITLE} text not null ,            
            ${Contract.StoryEntry.COLUMN_AUTHOR} text not null ,            
            ${Contract.StoryEntry.COLUMN_DESCRIPTION} text not null ,            
            ${Contract.StoryEntry.COLUMN_IMAGE_URL} text not null ,            
            ${Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL} text not null ,            
            ${Contract.StoryEntry.COLUMN_CREATED_DATE} text not null ,            
            ${Contract.StoryEntry.COLUMN_SCORE} text not null ,            
            ${Contract.StoryEntry.COLUMN_IS_TEXT_STORY} integer not null ,            
            foreign key (${Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID})  
            )
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        var createUserTable = """
            create table ${Contract.UserEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
            ${Contract.UserEntry.COLUMN_USERNAME} text not null,
            ${Contract.UserEntry.COLUMN_PASSWORD} text not null,
            ${Contract.UserEntry.COLUMN_NICKNAME} text not null,
            ${Contract.UserEntry.COLUMN_CREATED_DATE} text not null,
            ${Contract.UserEntry.COLUMN_ROLE_ID_FK} integer not null,
            foreign key (${Contract.UserEntry.COLUMN_ROLE_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID}))
        """.trimIndent()
        //////////////////////////
        //////////////////////////

        var createGenreTable = """
            create table ${Contract.GenreEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
            ${Contract.GenreEntry.COLUMN_NAME} text not null,
            ${Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK} integer not null,
            foreign key (${Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID}))
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        var createStoryGernTable = """
            create table ${Contract.StoryGenreEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
             ${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK} integer not null,
             ${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK} integer not null,
            foreign key (${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK}) references ${Contract.StoryEntry.TABLE_NAME}(${_ID}),
            foreign key (${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK}) references ${Contract.GenreEntry.TABLE_NAME}(${_ID}))
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        var createRoleTable = """
            create table ${Contract.RoleEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
            ${Contract.RoleEntry.COLUMN_NAME} text not null)
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        val createUserLoveStoryTable = """
    CREATE TABLE ${Contract.UserLoveStory.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.UserLoveStory.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.UserLoveStory.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.UserLoveStory.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.UserLoveStory.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createRateTable = """
    CREATE TABLE ${Contract.RateEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.RateEntry.COLUMN_RATE} INTEGER NOT NULL,
        ${Contract.RateEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.RateEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.RateEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.RateEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createCommentTable = """
    CREATE TABLE ${CommentEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${CommentEntry.COLUMN_CONTENT} TEXT NOT NULL,
        ${CommentEntry.COLUMN_TIME} TEXT NOT NULL,
        ${CommentEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${CommentEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${CommentEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${CommentEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createChapterTable = """
    CREATE TABLE ${Contract.ChapterEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ChapterEntry.COLUMN_TITLE} TEXT NOT NULL,
        ${Contract.ChapterEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ChapterEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createChapterMarkTable = """
    CREATE TABLE ${Contract.ChapterMarkEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ChapterMarkEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ChapterMarkEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createChapterHistoryTable = """
    CREATE TABLE ${Contract.ChapterHistoryEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createParagraphTable = """
    CREATE TABLE ${Contract.ParagraphEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ParagraphEntry.COLUMN_CONTENT_FILE} TEXT NOT NULL,
        ${Contract.ParagraphEntry.COLUMN_NUMBER_ORDER} INTEGER NOT NULL,
        ${Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createImageTable = """
    CREATE TABLE ${Contract.ImageEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ImageEntry.COLUMN_CONTENT_FILE} TEXT NOT NULL,
        ${Contract.ImageEntry.COLUMN_NUMBER_ORDER} INTEGER NOT NULL,
        ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createReadHistoryTable = """
    CREATE TABLE ${Contract.ReadHistory.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ReadHistory.COLUMN_DATE_TIME} TEXT NOT NULL,
        ${Contract.ReadHistory.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.ReadHistory.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ReadHistory.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.ReadHistory.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        p0?.execSQL(createReadHistoryTable)
        p0?.execSQL(createImageTable)
        p0?.execSQL(createChapterHistoryTable)
        p0?.execSQL(createChapterMarkTable)
        p0?.execSQL(createChapterTable)
        p0?.execSQL(createCommentTable)
        p0?.execSQL(createRateTable)
        p0?.execSQL(createUserLoveStoryTable)
        p0?.execSQL(createRoleTable)
        p0?.execSQL(createGenreTable)
        p0?.execSQL(createUserTable)
        p0?.execSQL(createStoryTable)
        p0?.execSQL(createStoryGernTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
//Thao tác với bảng User
    fun insertUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.UserEntry.COLUMN_USERNAME, user.userName)
            put(Contract.UserEntry.COLUMN_PASSWORD, user.hashedPw)
            put(Contract.UserEntry.COLUMN_NICKNAME, user.nickName)
            put(Contract.UserEntry.COLUMN_CREATED_DATE, user.createdDate)
            put(Contract.UserEntry.COLUMN_ROLE_ID_FK, user.roleID)
        }
        return db.insert(Contract.UserEntry.TABLE_NAME, null, values)
    }

    fun insertRole(role: Role): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.RoleEntry.COLUMN_NAME, role.roleName)
        }
        return db.insert(Contract.RoleEntry.TABLE_NAME, null, values)
    }
}
