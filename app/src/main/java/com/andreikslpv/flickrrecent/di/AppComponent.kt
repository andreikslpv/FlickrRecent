package com.andreikslpv.flickrrecent.di

import android.content.Context
import com.andreikslpv.flickrrecent.presentation.ui.MainActivity
import com.andreikslpv.flickrrecent.presentation.ui.fragments.GalleryFragment
import com.andreikslpv.flickrrecent.presentation.ui.fragments.PhotoFragment
import com.andreikslpv.flickrrecent.presentation.ui.utils.AlarmReceiver
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, RemoteModule::class, DatabaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(photoFragment: PhotoFragment)
    fun inject(galleryFragment: GalleryFragment)
    fun inject(alarmReceiver: AlarmReceiver)
}