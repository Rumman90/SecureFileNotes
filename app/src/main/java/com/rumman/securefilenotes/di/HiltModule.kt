package com.rumman.securefilenotes.di

import android.content.Context
import com.rumman.securefilenotes.data.files.FileHelperImpl
import com.rumman.securefilenotes.data.files.FilesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {


    @Singleton
    @Provides
    fun provideFileHelperInstance(@ApplicationContext appContext: Context) : FilesHelper {
        return FileHelperImpl(appContext)
    }

}