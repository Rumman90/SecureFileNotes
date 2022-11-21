package com.rumman.securefilenotes.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rumman.securefilenotes.BuildConfig
import com.rumman.securefilenotes.R
import com.rumman.securefilenotes.databinding.ActivityMainBinding
import com.rumman.securefilenotes.utils.showToast
import javax.crypto.KeyGenerator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.authenticate.setOnClickListener {
            if(binding.passwordEdittext.text.toString().trim() == BuildConfig.User_Password){
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                showToast(resources.getString(R.string.password_not_match))
            }

        }

    }
}