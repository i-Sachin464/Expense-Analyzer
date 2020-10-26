package com.client.expenseanalyzer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for splash screen
 */
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, TransactionActivity::class.java))
        finish()
    }
}