package com.example.worldstory.dat.admin_viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import org.mindrot.jbcrypt.BCrypt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserViewModel(val db: DatabaseHelper) : ViewModel() {
    private val __users = MutableLiveData<List<User>>()
    val _users: LiveData<List<User>> get() = __users
    val userName = MutableLiveData<String>()
    val passWord = MutableLiveData<String>()
    val nickName = MutableLiveData<String>()
    var roleID: Int = 0

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        __users.value=db.getAllUsers()
    }


    fun onAddUser():Long {
        val hashedpw = hashPassword(password = passWord.value.toString())
        val user = User(
            null,
            userName.value.toString(),
            hashedpw,
            SampleDataStory.getExampleImgURL(),
            nickName.value.toString(),
            roleID,
            dateTimeNow
        )
        val l: Long = db.insertUser(user)
        userName.value = ""
        passWord.value = ""
        nickName.value = ""
        Log.i("ham insert", "da insert")
        fetchAllUsers()
        return l
    }

    fun deleteAllUser() {
        db.deleteAllUser()
    }


    fun insertUser(user: User) {
        db.insertUser(user)
        fetchAllUsers()
    }

    fun updatetUser(user: User) {
        db.updateUser(user)
        fetchAllUsers()
    }

    fun delUser(user: User) {
        user.userID?.let { db.deleteUser(it) }
        fetchAllUsers()
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}

class UserViewModelFactory(private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}