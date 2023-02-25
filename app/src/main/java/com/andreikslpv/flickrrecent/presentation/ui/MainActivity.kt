package com.andreikslpv.flickrrecent.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.andreikslpv.flickrrecent.App
import com.andreikslpv.flickrrecent.R
import com.andreikslpv.flickrrecent.databinding.ActivityMainBinding
import com.andreikslpv.flickrrecent.domain.usecase.InitApplicationSettingsUseCase
import com.andreikslpv.flickrrecent.presentation.ui.fragments.GalleryFragment
import com.andreikslpv.flickrrecent.presentation.ui.fragments.PhotoFragment
import com.andreikslpv.flickrrecent.presentation.ui.utils.FragmentsType
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
}