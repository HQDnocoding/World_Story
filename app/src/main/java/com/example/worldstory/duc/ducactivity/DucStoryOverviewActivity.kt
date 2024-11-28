package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucStoryOverviewBinding
import com.example.worldstory.duc.ducutils.changeBackgroundTintColorByScore
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.getKey_chapterInfo
import com.example.worldstory.duc.ducutils.getKey_mainChapter
import com.example.worldstory.duc.ducutils.getKey_nextChapter
import com.example.worldstory.duc.ducutils.getKey_previousChapter
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducutils.toActivityStoriesByGenre
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel
import com.example.worldstory.duc.ducviewmodel.DucGenreViewModel
import com.example.worldstory.duc.ducviewmodel.DucRateViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucRateViewModelFactory
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Story

class DucStoryOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucStoryOverviewBinding
    private lateinit var storyInfo: Story
    private val ducChapterViewModel: DucChapterViewModel by viewModels {
        DucChapterViewModelFactory(this)
    }
    private val ducGenreViewModel: DucGenreViewModel by viewModels {
        DucGenreViewModelFactory(this)
    }
    private val ducRateViewModel: DucRateViewModel by viewModels {
       DucRateViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucStoryOverviewBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        setButtonWithOutData()
        //-------------
        var key = getKeyStoryInfo(this)

        if (checkloadInfoStory(key)) {
            loadInfoStory(key)
            setGenreButton()
            setRatingBar()
        } else {
            Toast.makeText(this, resources.getString(R.string.storyDataNotFound), Toast.LENGTH_LONG)
                .show()
        }

    }



    private fun setGenreButton() {
        ducGenreViewModel.fetchGenresByStory(storyInfo.storyID ?: numDef)
        ducGenreViewModel.genresByStory.observe(this, Observer { genresByStory ->

            for (genre in genresByStory) {
                var genreButton = AppCompatButton(this)
                setStyleGenreButton(genreButton, genre)

                genreButton.setOnClickListener {
                    this.toActivityStoriesByGenre(storyInfo.isTextStory.toBoolean(), genre)
                }


                binding.flexboxContainerGenreButtonStoryOverview.addView(genreButton)

            }
        })

    }

    private fun setStyleGenreButton(genreButton: AppCompatButton, genre: Genre) {
        genreButton.apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
            }
            setPadding(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
            setTextColor(
                ContextCompat.getColorStateList(
                    context,
                    R.color.selector_button_text_color_primary
                )
            )
            background = ContextCompat.getDrawable(context, R.drawable.shape_button_primary)
            text = genre.genreName
        }
    }

    fun checkloadInfoStory(key: String): Boolean {
        return intent.hasExtra(key)
    }

    fun loadInfoStory(key: String) {
        storyInfo = intent.getParcelableExtra<Story>(key) as Story
        binding.txtTitleStoryStoryOverview.text = storyInfo.title
        binding.txtAuthorStoryStoryOverview.text = storyInfo.author
        binding.txtDescriptionStoryStoryOverview.text = storyInfo.description
        binding.imgStoryStoryOverview.loadImgURL(this, storyInfo.imgUrl)
        binding.imgBackgroundStoryStoryOverview.loadImgURL(this, storyInfo.bgImgUrl)
        binding.txtScoreStoryStoryOverview.text = storyInfo.score.toString()
        generateChapter(storyInfo)

    }

    fun generateChapter(story: Story) {
        ducChapterViewModel.setChaptersByStory(storyInfo.storyID ?: 1)
        ducChapterViewModel.chaptersByStory.observe(this, Observer() { chapters ->


            binding.lineaerlistChapterStoryOverview.removeAllViews()
            for (item in chapters) {
                // Inflate each item view
                val itemView = LayoutInflater.from(this)
                    .inflate(
                        R.layout.list_item_chapter_story_overview_layout,
                        binding.lineaerlistChapterStoryOverview,
                        false
                    )

                // Set up itemView data if needed
                setItemViewChapter(itemView, item)
                // Add itemView to the container
                binding.lineaerlistChapterStoryOverview.addView(itemView)
            }
        })

    }

    private fun setItemViewChapter(itemView: View, chapter: Chapter) {
        val titleTextView =
            itemView.findViewById<TextView>(R.id.txtTitleChapter_listItemStoryOverview_layout)
        val idChapterTextView =
            itemView.findViewById<TextView>(R.id.txtIDChapter_listItemStoryOverview_layout)
        val dateCreatedTextView =
            itemView.findViewById<TextView>(R.id.txtDateCreatedChapter_listItemStoryOverview_layout)
        val btn = itemView.findViewById<LinearLayout>(R.id.btn_listItemStoryOverview_layout)
        titleTextView.text = chapter.title
        idChapterTextView.text = chapter.chapterID.toString()
        dateCreatedTextView.text = chapter.dateCreated.toString()
        btn.setOnClickListener {
            // tao key de chuyen cac chuong truoc, sau ,hien tai cho chapter activity
            toChapterActivity(chapter)

        }
    }

    private fun toChapterActivity(chapter: Chapter) {
        var key = getKey_chapterInfo(this)

        var key_mainChapter = getKey_mainChapter(this)
        var key_nextChapter = getKey_nextChapter(this)
        var key_previousChapter = getKey_previousChapter(this)
        var key_storyInfo = getKeyStoryInfo(this)

        var bundle = Bundle()
        bundle.putParcelable(key_mainChapter, chapter)
        bundle.putParcelable(
            key_nextChapter,
            ducChapterViewModel.getNextChapterByCurrentChapter(chapter)
        )
        bundle.putParcelable(
            key_previousChapter,
            ducChapterViewModel.getPreviousChapterByCurrentChapter(chapter)
        )
        bundle.putParcelable(
            key_storyInfo,
            storyInfo
        )
        this.toActivity(DucChapterActivity::class.java, key, bundle)
    }
    private fun setRatingBar() {
        ducRateViewModel.setRateByStory(storyInfo.storyID?:numDef)
        ducRateViewModel.ratingsByStory.observe(this, Observer{
            ratings->
            var averageScore: Float
            if(ratings.isEmpty()){
                averageScore=5.0f
            }else{ratings.stream()
                 averageScore = ratings.map { it.score }.average().toFloat()

            }
            binding.txtScoreStoryStoryOverview.text=averageScore.toString()
            binding.txtScoreStoryStoryOverview.changeBackgroundTintColorByScore(averageScore)

            var scoreUserSessionRated=ducRateViewModel.getScoreRateByUserSession()
            if(scoreUserSessionRated<0f)
            {
                //user hien tai chua danh gia
                binding.rateBarStoryOverview.rating=0f

            }else{
                binding.rateBarStoryOverview.rating=scoreUserSessionRated

            }
        })

        binding.rateBarStoryOverview.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if(fromUser)
            {
                ducRateViewModel.ratingStoryByCurrentUser(storyInfo.storyID?:numDef, rating.toInt())

            }
        }


    }
    fun setButtonWithOutData() {
        binding.btnBackSotryOverview.setOnClickListener {
            finish()
        }
    }
}