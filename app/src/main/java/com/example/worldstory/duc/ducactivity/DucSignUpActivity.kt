package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucSignUpBinding
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.UserLoginStateEnum
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucAccountManagerViewModelFactory
import com.example.worldstory.model.User
import kotlin.getValue


class DucSignUpActivity : AppCompatActivity() {
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels {
        DucAccountManagerViewModelFactory(this)
    }
    private lateinit var binding: ActivityDucSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucSignUpBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)

        //nguoi dung bam nut
        setConfigButton()

        // sau khi lay duoc du lieu kiem tra ten dang nhap co ton tai hay khong
        ducAccountManagerViewModel.checkAccountExist.observe(this, Observer { check ->
            if (check == UserLoginStateEnum.USERNAME_ALREADY_EXISTS) {
                binding.inputLayoutUsernameSignup.error = getString(R.string.usernameAlreadyExists)
                binding.inputLayoutComfirmpasswordSignup.error = null

            } else if (check == UserLoginStateEnum.CORRECT) {
                // neu tenn dang nhap chua ton tai, tiep tuc kiem tra mat khau bi trung
                var password = binding.etxtPasswordSignup.text.toString().trim()
                var confirmPassword = binding.etxtConfirmPasswordSignup.text.toString().trim()
                if (password != confirmPassword) {
                    // mat khau khac, hien canh bao
                    binding.inputLayoutComfirmpasswordSignup.error =
                        getString(R.string.comfirmPasswordDoesNotMatchPassword)
                    binding.inputLayoutUsernameSignup.error = null

                } else {
                    // dang ky thanh cong
                    //luu thong tin dang ky
                    saveDataUserSignup()

                    //chuyen huong ve man hinh dang nhap
                    toActivity(DucLoginActivity::class.java)
                    finish()
                }
            }
        })
    }

    private fun setConfigButton() {
        binding.btnSignupSignup.setOnClickListener {
            var username = binding.etxtUsernameSignup.text.toString().trim()
            var email = binding.etxtEmailSignup.text.toString().trim()

            ducAccountManagerViewModel.fetchCheckAccountExist(username)
        }
    }

    private fun DucSignUpActivity.saveDataUserSignup() {
        binding.inputLayoutUsernameSignup.error = null
        binding.inputLayoutComfirmpasswordSignup.error = null


        var username = binding.etxtUsernameSignup.text.toString().trim()
        var email = binding.etxtEmailSignup.text.toString().trim()
        var nickname = binding.etxtNicknameSignup.text.toString().trim()
        var password = binding.etxtPasswordSignup.text.toString().trim()

        ducAccountManagerViewModel.SignUpNewAccount(username,password,email,nickname)
    }

}

