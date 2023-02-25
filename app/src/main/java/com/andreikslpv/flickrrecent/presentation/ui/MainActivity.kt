package com.andreikslpv.flickrrecent.presentation.ui

//import androidx.fragment.app.activityViewModels
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.R
import com.andreikslpv.flickrrecent.databinding.ActivityMainBinding
import com.andreikslpv.flickrrecent.domain.usecase.InitApplicationSettingsUseCase
import com.andreikslpv.flickrrecent.presentation.ui.fragments.GalleryFragment
import com.andreikslpv.flickrrecent.presentation.ui.fragments.PhotoFragment
import com.andreikslpv.flickrrecent.presentation.ui.utils.CHANNEL_ID
import com.andreikslpv.flickrrecent.presentation.ui.utils.FragmentsType
import com.andreikslpv.flickrrecent.presentation.ui.utils.NOTIFICATION_TITLE
import com.andreikslpv.flickrrecent.presentation.vm.MainActivityViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var initApplicationSettingsUseCase: InitApplicationSettingsUseCase

    init {
        App.instance.dagger.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isActivityNotRunning = false
        createChannel(
            CHANNEL_ID,
            CHANNEL_ID
        )
        initApplicationSettings()
        initBottomNavigationMenu()
        // если первый, то запускаем фрагмент Photo
        if (savedInstanceState == null)
            changeFragment(PhotoFragment(), FragmentsType.PHOTO)

    }

    private fun initApplicationSettings() {
        // устанавливаем сохраненные настройки приложения
        initApplicationSettingsUseCase.execute()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = NOTIFICATION_TITLE

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    private fun initBottomNavigationMenu() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentPlaceholder)
            when (it.itemId) {
                R.id.photo -> {
                    if (currentFragment !is PhotoFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.PHOTO)
                        changeFragment(fragment ?: PhotoFragment(), FragmentsType.PHOTO)
                    }
                    true
                }
                R.id.gallery -> {
                    if (currentFragment !is GalleryFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.GALLERY)
                        changeFragment(fragment ?: GalleryFragment(), FragmentsType.GALLERY)
                    }
                    true
                }
                else -> false
            }
        }
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(type: FragmentsType): Fragment? =
        supportFragmentManager.findFragmentByTag(type.tag)

    private fun changeFragment(fragment: Fragment, type: FragmentsType) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, fragment, type.tag)
            .addToBackStack(null)
            .commit()
    }

    override fun onPause() {
        super.onPause()
        viewModel.isActivityNotRunning = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.isActivityNotRunning = false
    }
}