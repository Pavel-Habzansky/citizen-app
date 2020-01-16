package com.pavelhabzansky.citizenapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pavelhabzansky.citizenapp.core.activity.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
