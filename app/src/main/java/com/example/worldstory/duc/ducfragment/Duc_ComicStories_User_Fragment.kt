package com.example.worldstory.duc.ducfragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentDucComicStoriesUserBinding
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducactivity.DucSearchActivity
import com.example.worldstory.duc.ducadapter.Duc_Button_Adapter
import com.example.worldstory.duc.ducdatabase.DucDatabaseHelper
import com.example.worldstory.duc.ducutils.createGridCardViewStory
import com.example.worldstory.duc.ducutils.getKeyIsComic
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducviewmodel.DucGenreViewModel
import com.example.worldstory.duc.ducviewmodel.DucStoryViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucStoryViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Duc_ComicStories_User_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Duc_ComicStories_User_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentDucComicStoriesUserBinding
    private lateinit var recyclerViewGenreButton: RecyclerView
    private lateinit var linearLayout: LinearLayout
    private var isComic = true


    private val ducStoryViewModel: DucStoryViewModel by viewModels {
        DucStoryViewModelFactory(requireContext())
    }
    val ducGenreViewModel: DucGenreViewModel by viewModels {
        DucGenreViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDucComicStoriesUserBinding.inflate(layoutInflater)
        val view = binding.root
        linearLayout = binding.linearLayoutFragmentComicStoryUser
        recyclerViewGenreButton = binding.rvButtonGenreComicStoriesUser

        ///////////////////////
        var imgURL:String= "https://storage-ct.lrclib.net/file/cuutruyen/uploads/manga/2478/cover/processed-4079ee6ed3b108490e33fca63589c35e.jpg"
        var imgURL2:String="https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467618820_987540786516413_9022671729236654001_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=wcRr4uG_frQQ7kNvgG7AO3Q&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=ACWt9Nxlog48s7vciMKLvlm&oh=00_AYCFt2WZkt3Qy8rXYq0fnq2KdcuMdRdl7Qr9prRr8E71gw&oe=67420403"

       // var imgURL2:String="https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467637825_987540789849746_543458540327297711_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=iQOcKkq2qUYQ7kNvgEgyvuo&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=Aym2wPdWobnahsnjGHBJ8Oe&oh=00_AYDvl3AyQ-c3GFb3FLj7XK2t_c8cj8bY6POC_kkaENEi1A&oe=67420244"


//        binding.imgTestComicUserstory.load(imgURL)
//        Glide.with(this){
//            .load(imgURL)
//            .into(binding.imgTestComicUserstory)
//        }
        //Glide.with(requireContext()).load(imgURL).centerCrop().into(binding.imgTestComicUserstory)
binding.imgTestComicUserstory.loadImgURL(requireContext(),imgURL2)
        recyclerViewGenreButton.layoutManager = GridLayoutManager(
            view.context, 1, GridLayoutManager.HORIZONTAL, false
        )
        recyclerViewGenreButton.adapter = Duc_Button_Adapter(
            //requireContext(), ArrayList(genreViewModel.genres.value), isComic
            requireContext(), ArrayList(ducGenreViewModel.getAllGenres()), isComic
        )

        var searchImgBtn = binding.searchButtonComicStoriesUser

        searchImgBtn.setOnClickListener {
            toSearchActivity()
        }


        for (genre in ducGenreViewModel.getAllGenres()) {

            createGridCardViewStory(
                requireContext(),
                inflater,
                linearLayout,
                genre,
                ducStoryViewModel.getComicStoriesByGenre(genre)
            )
        }
       // testDatabase()


//        genreViewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
//
//
//            for (i in genres.indices) {
//                if(requireContext() != null && inflater != null && genres[i] != null && storiesViewModel.getComicStoriesByGenre(genres[i]) != null)
//                {
//                    createGridCardViewStory(
//                        requireContext(),
//                        inflater,
//                        linearLayout,
//                        genres[i],
//                        storiesViewModel.getComicStoriesByGenre(genres[i])
//                    )
//                }
//
//
//            }
//
//        })
        ///////////////////////

//       ---------------------------------------------------------------------------------
        // Inflate the layout for this fragment
        return view
    }

    private fun testDatabase() {
        var ducDataHelper: DucDatabaseHelper= DucDatabaseHelper(requireContext())
        ducDataHelper.insertStory(ducStoryViewModel.getOneExampleStory())

        binding.txtTestComicUserstory.text=ducDataHelper.getAllStory().toString()
    }

    fun toSearchActivity() {
        var intent = Intent(context, DucSearchActivity::class.java)
        intent.putExtra(getKeyIsComic(requireContext()), isComic)
        startActivity(intent)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ComicStoriesUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Duc_ComicStories_User_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}