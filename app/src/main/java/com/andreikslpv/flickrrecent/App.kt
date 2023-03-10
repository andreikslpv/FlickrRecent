package com.andreikslpv.flickrrecent

import android.app.Application
import com.andreikslpv.flickrrecent.di.AppComponent
import com.andreikslpv.flickrrecent.di.DaggerAppComponent

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()

        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        dagger = DaggerAppComponent.factory().create(this)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            private set
    }

}