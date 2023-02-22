package com.andreikslpv.flickrrecent.di

import android.content.Context
import com.andreikslpv.flickrrecent.presentation.vm.PhotoFragmentViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, DataModule::class, RemoteModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(photoFragmentViewModel: PhotoFragmentViewModel)
}