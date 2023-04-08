package com.itstor.githubapp.ui.view.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.itstor.githubapp.R
import com.itstor.githubapp.data.SettingPreferences
import com.itstor.githubapp.databinding.ActivitySplashBinding
import com.itstor.githubapp.ui.view.main.MainActivity
import com.itstor.githubapp.ui.view.main.MainViewModel
import com.itstor.githubapp.ui.view.main.MainViewModelFactory
import com.itstor.githubapp.ui.view.main.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    private lateinit var settingPreferences: SettingPreferences
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreferences = SettingPreferences.getInstance(dataStore)

        viewModel = obtainViewModel(this)

        supportActionBar?.hide()

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val splashTimeOut = 2000L
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashTimeOut)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): SplashViewModel {
        val factory = SplashViewModelFactory.getInstance(settingPreferences)
        return ViewModelProvider(activity, factory)[SplashViewModel::class.java]
    }
}