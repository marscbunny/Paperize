package com.anthonyla.paperize.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.anthonyla.paperize.data.settings.SettingsDataStore
import com.anthonyla.paperize.data.settings.SettingsDataStoreImpl
import com.anthonyla.paperize.feature.wallpaper.data.data_source.AlbumDatabase
import com.anthonyla.paperize.feature.wallpaper.data.repository.AlbumRepositoryImpl
import com.anthonyla.paperize.feature.wallpaper.domain.repository.AlbumRepository
import com.anthonyla.paperize.feature.wallpaper.use_case.AddAlbum
import com.anthonyla.paperize.feature.wallpaper.use_case.AddImage
import com.anthonyla.paperize.feature.wallpaper.use_case.AlbumUseCases
import com.anthonyla.paperize.feature.wallpaper.use_case.DeleteAlbum
import com.anthonyla.paperize.feature.wallpaper.use_case.DeleteImage
import com.anthonyla.paperize.feature.wallpaper.use_case.GetAlbums
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAlbumDatabase(app: Application): AlbumDatabase {
        return Room.databaseBuilder(
            app,
            AlbumDatabase::class.java,
            AlbumDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(
        db: AlbumDatabase
    ): AlbumRepository {
        return AlbumRepositoryImpl(db.albumDao)
    }

    @Provides
    @Singleton
    fun provideAlbumUseCases(
        repository: AlbumRepository
    ): AlbumUseCases {
        return AlbumUseCases (
            getAlbums = GetAlbums(repository),
            deleteAlbum = DeleteAlbum(repository),
            deleteImage = DeleteImage(repository),
            addAlbum = AddAlbum(repository),
            addImage = AddImage(repository),
        )
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore (
        @ApplicationContext context: Context
    ): SettingsDataStore = SettingsDataStoreImpl(context)

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }
}