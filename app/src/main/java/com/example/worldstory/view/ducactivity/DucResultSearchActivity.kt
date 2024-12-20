package com.example.worldstory.view.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityDucResultSearchBinding
import com.example.worldstory.duc.ducadapter.Duc_CardStoryItem_Adapter
import com.example.worldstory.data.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducutils.getDataNotFound
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getKeyResultSearchInfo
import com.example.worldstory.duc.ducutils.getKeyTextQuery
import com.example.worldstory.view_models.ducviewmodel.DucStoryViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucStoryViewModelFactory

class DucResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucResultSearchBinding
    private lateinit var dataList: ArrayList<DucStoryDataClass>
    private lateinit var textQuery: String
    private var isText: Boolean = false
    private val ducStoryViewModel: DucStoryViewModel by viewModels {
        DucStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDucResultSearchBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        //===============================
        if (isCheckLoad(getKeyResultSearchInfo(this)) == false) {
            return
        }

        loadData()
        setData()
        setConfigButton()


    }

    private fun setData() {
        ducStoryViewModel.stories.observe(this, Observer { stories ->
            var cardStoryAdapter = Duc_CardStoryItem_Adapter(
                this,
                ArrayList(ducStoryViewModel.getStoriesByQuery(textQuery, isText))
            )
            binding.recyclerStoriesResultSearch.apply {
                adapter = cardStoryAdapter
                layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            }

        })

        //recyclerView.setHasFixedSize(true)
    }

    private fun setConfigButton() {
        binding.btnBackResultSearch.setOnClickListener {
            finish()
        }
    }

    private fun isCheckLoad(key: String): Boolean {
        return intent.hasExtra(key)
    }

    private fun loadData() {
        var bundle = intent.getBundleExtra(getKeyResultSearchInfo(this))
        if (bundle != null) {
            isText = bundle.getBoolean(getKeyIsText(this))
            textQuery = bundle.getString(getKeyTextQuery(this)) ?: getDataNotFound(this)

        }
    }
}