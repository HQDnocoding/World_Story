package com.example.worldstory

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.viewModels

import androidx.fragment.app.Fragment
import com.example.worldstory.dat.admin_view_navs.GenreFragment
import com.example.worldstory.dat.admin_view_navs.CommentFragment
import com.example.worldstory.dat.admin_view_navs.StoryFragment
import com.example.worldstory.dat.admin_view_navs.UserFragment
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Role
import com.google.android.material.appbar.MaterialToolbar


class AdminMainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast
    private val roleViewModel: RoleViewModel by viewModels {
        RoleViewModelFactory(DatabaseHelper(this))
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(DatabaseHelper(this))
    }
    private val genreViewModel: GenreViewModel by viewModels {
        GenreViewModelFactory(DatabaseHelper(this))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

//        roleViewModel.deleteAllRole()
//        roleViewModel.insertRole(Role(null,"ADMIN"))
//        roleViewModel.insertRole(Role(null,"AUTHOR"))
//        roleViewModel.insertRole(Role(null,"MEMBER"))
//        roleViewModel.insertRole(Role(null,"GUEST"))

        //Thiết lập sự kiện thông báo nhấn nút add
        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        topAppBar.setOnMenuItemClickListener() { menuitem ->
            when (menuitem.itemId) {
                R.id.add_button -> {
                    sharedViewModel.onAddButtonClicked()
                    true
                }

                R.id.search_button -> {
                    sharedViewModel.onSearch()
                    true
                }

                else -> false
            }
        }
        //đồng bộ navigationBottom với navgraph
        if (savedInstanceState == null) {
            loadFragment(UserFragment(), "user")
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val bundle =Bundle()
//        val userID=this.getUserIdSession()
//        val roleCurrentUser=userViewModel.getUser(userID).roleID
//        val user=userViewModel.getUser(userID)
        val currentRole="ADMIN"
//        bundle.putInt("currentID",userID)
        bundle.putString("currentRole",currentRole)
        bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.userFragment -> {
                    val userFragment=UserFragment()
                    userFragment.arguments=bundle
                    loadFragment(userFragment, "user")
                    true
                }

                R.id.categoryFragment -> {
                    val genreFragment=GenreFragment()
                    genreFragment.arguments=bundle
                    loadFragment(genreFragment, "category")
                    true
                }

                R.id.storyFragment -> {
                    val storyFragment=StoryFragment()
                    storyFragment.arguments=bundle
                    loadFragment(storyFragment, "story")
                    true
                }

                R.id.commentFragment -> {
                    val commentFragment=CommentFragment()
                    commentFragment.arguments=bundle
                    loadFragment(commentFragment, "comment")
                    true
                }

                else -> false
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            when (supportFragmentManager.findFragmentById(R.id.host_fragment)?.tag) {
                "user" -> bottomNavigationView.selectedItemId = R.id.userFragment
                "category" -> bottomNavigationView.selectedItemId = R.id.categoryFragment
                "story" -> bottomNavigationView.selectedItemId = R.id.storyFragment
                "comment" -> bottomNavigationView.selectedItemId = R.id.commentFragment
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            val currentTime = System.currentTimeMillis()
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else if (currentTime - backPressedTime < 2000) {
                toast.cancel()
                finish()
            } else {
                backPressedTime = currentTime
                toast =
                    Toast.makeText(
                        this@AdminMainActivity,
                        "Bấm lần nữa để thoát",
                        Toast.LENGTH_SHORT
                    )
                toast.show()
            }
        }

    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val existingFragment = fragmentManager.findFragmentByTag(tag)

        if (existingFragment == null) {
            // Chỉ thêm nếu chưa tồn tại
            fragmentManager.beginTransaction().apply {
                replace(R.id.host_fragment, fragment, tag)
                addToBackStack(tag) // Sử dụng tag để quản lý BackStack
                commit()
            }
        } else {
            // Nếu đã tồn tại, pop BackStack về fragment đó
            fragmentManager.popBackStack(tag, 0)
        }
    }

    override fun onDestroy() {
        DatabaseHelper(this).close()
        super.onDestroy()
    }
}