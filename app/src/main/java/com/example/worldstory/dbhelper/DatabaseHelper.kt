package com.example.worldstory.dbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


object Contract {
    //Story table
    object StoryEntry : BaseColumns {
        private const val TABLE_NAME = "story_table"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URL = "image_url"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_CREATED_DATE = "created_date"
        private const val COLUMN_IS_TEXT_STORY = "is_text_story"

        // Foreign key
        private const val COLUMN_GENRE_ID_FK = "genre_id"
    }

    //Genre table
    object GenreEntry : BaseColumns {
        private const val TABLE_NAME = "genre_table"
        private const val COLUMN_NAME = "name"

        //Foreign key
        private const val COLUMN_USER_ID_FK = "user_id"
    }

    //User table
    object UserEntry : BaseColumns {
        private const val TABLE_NAME = "user_table"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NICKNAME = "nickname"
        private const val COLUMN_CREATED_DATE = "created_date"

        //Foreign key
        private const val COLUMN_ROLE_ID_FK = "role_id"
    }

    //Role table
    object RoleEntry : BaseColumns {
        private const val TABLE_NAME = "role_table"
        private const val COLUMN_NAME = "name"
    }

    //Rate table
    object RateEntry : BaseColumns {
        private const val TABLE_RATE = "rate_table"
        private const val COLUMN_RATE = "rate"

        //Foreign key
        private const val COLUMN_USER_ID_FK = "user_id"
        private const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Comment table
    object CommentEntry : BaseColumns {
        private const val TABLE_NAME = "comment_table"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_TIME = "time"

        //Foreign key
        private const val COLUMN_USER_ID_FK = "user_id"
        private const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Chapter table
    object ChapterEntry : BaseColumns {
        private const val TABLE_NAME = "chapter_table"
        private const val COLUMN_TITLE = "title"

        //Foreign key
        private const val COLUMN_USER_ID_FK = "user_id"
        private const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Paragraph table
    object ParagraphEntry : BaseColumns {
        private const val TABLE_NAME = "paragraph_table"
        private const val COLUMN_CONTENT_FILE = "text_url"
        private const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        private const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Imange table
    object ImageEntry:BaseColumns{
        private const val TABLE_NAME = "img_table"
        private const val COLUMN_CONTENT_FILE = "img_url"
        private const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        private const val COLUMN_STORY_ID_FK = "story_id"
    }

    //ReadHistory table
    object ReadHistory:BaseColumns{
        private const val TABLE_NAME="history_read_table"
        private const val COLUMN_DATE_TIME="date_time"
        //Foreign
        private const val COLUMN_USER_ID_FK = "user_id"
        private const val COLUMN_STORY_ID_FK = "story_id"
    }
}

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_doc_truyen_db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}