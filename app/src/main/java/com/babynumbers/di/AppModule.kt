package com.babynumbers.di

import android.content.Context
import com.babynumbers.audio.AsrManager
import com.babynumbers.audio.AudioPlayer
import com.babynumbers.audio.SoundManager
import com.babynumbers.data.repository.LearningRepository
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
    fun provideLearningRepository(
        @ApplicationContext context: Context
    ): LearningRepository {
        return LearningRepository(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: Context
    ): AudioPlayer {
        return AudioPlayer(context)
    }

    @Provides
    @Singleton
    fun provideAsrManager(
        @ApplicationContext context: Context
    ): AsrManager {
        return AsrManager(context)
    }

    @Provides
    @Singleton
    fun provideSoundManager(
        @ApplicationContext context: Context
    ): SoundManager {
        return SoundManager(context)
    }
}
