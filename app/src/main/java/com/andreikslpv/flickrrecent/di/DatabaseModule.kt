package com.andreikslpv.flickrrecent.di

import com.andreikslpv.flickrrecent.data.db.PhotoRealmModel
import dagger.Module
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesRealmConfig(): RealmConfiguration =
        RealmConfiguration.create(setOf(PhotoRealmModel::class))

    @Singleton
    @Provides
    fun providesRealmDb(configuration: RealmConfiguration): Realm = Realm.open(configuration)
}