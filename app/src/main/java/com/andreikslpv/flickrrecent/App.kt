package com.andreikslpv.flickrrecent

import android.app.Application
import com.andreikslpv.flickrrecent.di.AppComponent
import com.andreikslpv.flickrrecent.di.DaggerAppComponent

class App : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.factory().create(applicationContext)
    }

}