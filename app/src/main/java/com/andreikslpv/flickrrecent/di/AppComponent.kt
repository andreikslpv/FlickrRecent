package com.andreikslpv.flickrrecent.di

import android.content.Context
import com.andreikslpv.flickrrecent.presentation.ui.utils.AlarmReceiver
import com.andreikslpv.flickrrecent.presentation.vm.GalleryViewModel
import com.andreikslpv.flickrrecent.presentation.vm.MainViewModel
import com.andreikslpv.flickrrecent.presentation.vm.PhotoViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, DataModule::class, RemoteModule::class, DatabaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainViewModel: MainViewModel)
    fun inject(photoViewModel: PhotoViewModel)
    fun inject(galleryViewModel: GalleryViewModel)
    fun inject(alarmReceiver: AlarmReceiver)
}