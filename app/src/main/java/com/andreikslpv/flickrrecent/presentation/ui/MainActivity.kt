package com.andreikslpv.flickrrecent.presentation.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.R
import com.andreikslpv.flickrrecent.databinding.ActivityMainBinding
import com.andreikslpv.flickrrecent.domain.usecase.InitApplicationSettingsUseCase
import com.andreikslpv.flickrrecent.presentation.ui.fragments.GalleryFragment
import com.andreikslpv.flickrrecent.presentation.ui.fragments.PhotoFragment
import com.andreikslpv.flickrrecent.presentation.ui.utils.CHANNEL_ID
import com.andreikslpv.flickrrecent.presentation.ui.utils.FragmentsType
import com.andreikslpv.flickrrecent.presentation.ui.utils.NOTIFICATION_TITLE
import com.andreikslpv.flickrrecent.presentation.ui.utils.makeToast
import com.andreikslpv.flickrrecent.presentation.vm.MainActivityViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var initApplicationSettingsUseCase: InitApplicationSettingsUseCase

    private val singlePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    granted -> {
                        // уведомления разрешены
                    }
                    !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                        // уведомления запрещены, пользователь поставил галочку Don't ask again.
                        // сообщаем пользователю, что он может в дальнейшем разрешить уведомления
                        getString(R.string.details_allow_later_in_settings).makeToast(this)
                    }
                    else -> {
                        // уведомления запрещены, пользователь отклонил запрос
                    }
                }
            }
        }


    init {
        App.instance.dagger.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setActivityStatusIsNotRunning(false)
        createChannel(CHANNEL_ID, CHANNEL_ID)

        initApplicationSettings()
        initBottomNavigationMenu()
        // если первый, то запускаем фрагмент Photo
        if (savedInstanceState == null)
            changeFragment(PhotoFragment(), FragmentsType.PHOTO)
        requestPermission()
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
                        changeFragment(PhotoFragment(), FragmentsType.PHOTO)
                        viewModel.setActivityStatusIsNotRunning(false)
                    }
                    true
                }
                R.id.gallery -> {
                    if (currentFragment !is GalleryFragment) {
                        changeFragment(GalleryFragment(), FragmentsType.GALLERY)
                        viewModel.setActivityStatusIsNotRunning(false)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment, type: FragmentsType) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, fragment, type.tag)
            .addToBackStack(null)
            .commit()
    }

    private fun requestPermission() {
        // если Андройд 13+ то запрашиваем разрешение на показ уведомлений
        if (Build.VERSION.SDK_INT >= 33) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // уведомления запрещены, нужно объяснить зачем нам требуется разрешение
                singlePermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                singlePermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.setActivityStatusIsNotRunning(true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setActivityStatusIsNotRunning(false)
    }
}